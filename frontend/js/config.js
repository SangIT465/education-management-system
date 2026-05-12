// Đổi URL này thành URL backend Render sau khi deploy
// VD: 'https://quanlydaotaoa-backend.onrender.com/api/v1'
const API_BASE_URL = window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1'
  ? 'http://localhost:8080/api/v1'
  : 'https://education-management-systen-q9k6.onrender.com/api/v1';
