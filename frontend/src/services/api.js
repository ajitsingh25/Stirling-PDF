import axios from 'axios';

const apiClient = axios.create({
  baseURL: '/api/v1', // Assuming the backend is served on the same host
  headers: {
    'Content-Type': 'multipart/form-data',
  },
});

export default {
  mergePdfs(files) {
    const formData = new FormData();
    files.forEach(file => {
      formData.append('fileInput', file);
    });
    // Add other options as needed by the backend API
    formData.append('removeCertSign', 'false');
    formData.append('generateToc', 'false');
    formData.append('sortType', 'orderProvided');

    return apiClient.post('/general/merge-pdfs', formData, {
      responseType: 'blob', // Important for file downloads
    });
  },
};
