<template>
  <main class="home-view">
    <div class="container">
      <h1 class="title">Stirling PDF</h1>
      <p class="description">Your new online PDF editor</p>

      <div class="upload-section">
        <label for="file-upload" class="upload-label">
          <span class="upload-icon">📁</span>
          <span class="upload-text">Drag & drop files here, or click to select files</span>
        </label>
        <input id="file-upload" type="file" multiple @change="handleFileChange" />
      </div>

      <div v-if="files.length" class="file-list">
        <h2>Selected files:</h2>
        <ul>
          <li v-for="file in files" :key="file.name">{{ file.name }}</li>
        </ul>
        <button class="process-button" @click="processFiles">Process Files</button>
      </div>
    </div>
  </main>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const files = ref([]);
const router = useRouter();

function handleFileChange(event) {
  files.value = Array.from(event.target.files);
}

function processFiles() {
  if (files.value.length === 0) return;

  // For now, we'll just take the first file
  const file = files.value[0];
  const url = URL.createObjectURL(file);

  router.push({ name: 'editor', query: { pdf: url } });
}
</script>

<style scoped>
.home-view {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  text-align: center;
}

.container {
  max-width: 600px;
  width: 100%;
}

.title {
  font-size: 3rem;
  font-weight: bold;
  margin-bottom: 1rem;
}

.description {
  font-size: 1.2rem;
  color: var(--color-text-soft);
  margin-bottom: 2rem;
}

.upload-section {
  border: 2px dashed var(--color-border);
  border-radius: 10px;
  padding: 2rem;
  cursor: pointer;
  transition: background-color 0.2s;
}

.upload-section:hover {
  background-color: var(--color-background-soft);
}

.upload-label {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
}

.upload-icon {
  font-size: 3rem;
}

.upload-text {
  font-size: 1.1rem;
}

#file-upload {
  display: none;
}

.file-list {
  margin-top: 2rem;
  text-align: left;
}

.process-button {
  margin-top: 1rem;
  padding: 0.8rem 1.5rem;
  font-size: 1rem;
  background-color: var(--color-heading);
  color: var(--color-background);
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.process-button:hover {
  opacity: 0.9;
}
</style>
