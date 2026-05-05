/* =====================================================
   API CLIENT - Kết nối với Spring Boot Backend
   ===================================================== */

const API_BASE = 'http://localhost:8080/api/v1';

const Api = {
  /**
   * Helper: gọi fetch và xử lý response
   */
  async _fetch(url, options = {}) {
    try {
      const res = await fetch(url, {
        headers: { 'Content-Type': 'application/json' },
        ...options
      });
      const json = await res.json();
      if (!res.ok || !json.success) {
        throw new Error(json.message || `HTTP ${res.status}`);
      }
      return json.data;
    } catch (err) {
      console.error('[API ERROR]', err);
      throw err;
    }
  },

  /** Lấy tất cả sinh viên */
  getStudents() {
    return this._fetch(`${API_BASE}/students`);
  },

  /** Lấy đợt đăng ký đang mở */
  getOpenPeriods() {
    return this._fetch(`${API_BASE}/registration-periods/open`);
  },

  /** Lấy danh sách môn cần học lại của sinh viên */
  getRetakeCourses(studentId) {
    return this._fetch(`${API_BASE}/retake/students/${studentId}/courses`);
  },

  /** Đăng ký học phần */
  register({ studentId, courseSectionId, registrationPeriodId, registrationType, note }) {
    return this._fetch(`${API_BASE}/registrations`, {
      method: 'POST',
      body: JSON.stringify({
        studentId,
        courseSectionId,
        registrationPeriodId,
        registrationType: registrationType || 'RETAKE',
        note: note || ''
      })
    });
  },

  /** Hủy đăng ký */
  cancelRegistration(registrationId) {
    return this._fetch(`${API_BASE}/registrations/${registrationId}`, {
      method: 'DELETE'
    });
  },

  /** Lấy danh sách đăng ký của sinh viên */
  getStudentRegistrations(studentId) {
    return this._fetch(`${API_BASE}/registrations/students/${studentId}`);
  },

  /** Lấy tất cả lớp học phần đang mở */
  getCourseSections() {
    return this._fetch(`${API_BASE}/course-sections/open`);
  }
};
