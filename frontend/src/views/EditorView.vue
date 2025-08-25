<template>
  <div class="editor-view">
    <div class="toolbar">
      <button @click="triggerMergeInput">Merge</button>
      <input
        ref="mergeInput"
        type="file"
        multiple
        @change="handleMerge"
        style="display: none"
      />
      <button>Split</button>
      <button>Rotate</button>
    </div>
    <div class="viewer-container">
      <PdfViewer v-if="pdfUrl" :url="pdfUrl" />
      <div v-else class="no-pdf">
        <h2>No PDF selected</h2>
        <p>
          Please go back to the
          <RouterLink to="/">home page</RouterLink> to select a PDF.
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import PdfViewer from '../components/PdfViewer.vue';
import api from '../services/api';

const route = useRoute();
const pdfUrl = ref(null);
const currentFile = ref(null);
const mergeInput = ref(null);

function triggerMergeInput() {
  mergeInput.value.click();
}

async function handleMerge(event) {
  const newFiles = Array.from(event.target.files);
  if (newFiles.length === 0) return;

  const allFiles = [currentFile.value, ...newFiles];

  try {
    const response = await api.mergePdfs(allFiles);
    const blob = new Blob([response.data], { type: 'application/pdf' });
    const url = URL.createObjectURL(blob);
    pdfUrl.value = url;

    // Also update the current file for subsequent merges
    const newFileName = 'merged.pdf';
    currentFile.value = new File([blob], newFileName, { type: 'application/pdf' });

    // Optional: download the merged file
    // downloadFile(blob, 'merged.pdf');
  } catch (error) {
    console.error('Error merging PDFs:', error);
    alert('Failed to merge PDFs. See console for details.');
  }
}

function downloadFile(blob, filename) {
  const url = window.URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.style.display = 'none';
  a.href = url;
  a.download = filename;
  document.body.appendChild(a);
  a.click();
  window.URL.revokeObjectURL(url);
  document.body.removeChild(a);
}

onMounted(async () => {
  const url = route.query.pdf;
  if (url) {
    pdfUrl.value = url;
    // Fetch the blob to create a File object
    const response = await fetch(url);
    const blob = await response.blob();
    currentFile.value = new File([blob], 'original.pdf', { type: 'application/pdf' });
  }
});
</script>

<style scoped>
.editor-view {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.toolbar {
  display: flex;
  gap: 1rem;
  padding: 1rem;
  background-color: var(--color-background-soft);
  border-bottom: 1px solid var(--color-border);
}

.viewer-container {
  flex-grow: 1;
  overflow: hidden;
}

.no-pdf {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
  gap: 1rem;
}
</style>
