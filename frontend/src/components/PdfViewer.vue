<template>
  <div class="pdf-viewer">
    <canvas ref="canvas"></canvas>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import * as pdfjsLib from 'pdfjs-dist';

// Set workerSrc to load the worker from a CDN
pdfjsLib.GlobalWorkerOptions.workerSrc = `//cdnjs.cloudflare.com/ajax/libs/pdf.js/${pdfjsLib.version}/pdf.worker.min.js`;

const props = defineProps({
  url: {
    type: String,
    required: true,
  },
});

const canvas = ref(null);

async function renderPdf() {
  if (!props.url) return;

  const loadingTask = pdfjsLib.getDocument(props.url);
  const pdf = await loadingTask.promise;

  const page = await pdf.getPage(1);
  const viewport = page.getViewport({ scale: 1.5 });

  const canvasEl = canvas.value;
  const context = canvasEl.getContext('2d');
  canvasEl.height = viewport.height;
  canvasEl.width = viewport.width;

  const renderContext = {
    canvasContext: context,
    viewport: viewport,
  };
  await page.render(renderContext).promise;
}

onMounted(renderPdf);
watch(() => props.url, renderPdf);
</script>

<style scoped>
.pdf-viewer {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: auto;
}

canvas {
  max-width: 100%;
  max-height: 100%;
}
</style>
