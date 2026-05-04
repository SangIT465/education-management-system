/* =====================================================
   ADMIN PANEL - Quản lý dữ liệu
   ===================================================== */

const ADMIN_API = 'http://localhost:8080/api/v1/admin';
const MAIN_API  = 'http://localhost:8080/api/v1';

const S = {
  tab: 'students',
  students: [], courses: [], semesters: [], sections: [], periods: [], grades: [],
  editEntity: null, editId: null
};

// ====================== API HELPER ======================
async function apiFetch(url, opts = {}) {
  const res = await fetch(url, {
    headers: { 'Content-Type': 'application/json' },
    ...opts
  });
  const json = await res.json();
  if (!res.ok || !json.success) throw new Error(json.message || `HTTP ${res.status}`);
  return json.data;
}

// ====================== AUTH ======================
function checkAdminAuth() {
  try {
    const raw = localStorage.getItem('currentUser');
    if (!raw) { window.location.href = 'login.html'; return; }
    const user = JSON.parse(raw);
    if (!user || !user.role) { localStorage.removeItem('currentUser'); window.location.href = 'login.html'; return; }
    if (user.role !== 'admin') { window.location.href = 'index.html'; return; }
    const info = document.getElementById('adminUserInfo');
    if (info) info.innerHTML = `<span class="user-badge">${user.fullName || user.username}</span>`;
  } catch {
    localStorage.removeItem('currentUser');
    window.location.href = 'login.html';
  }
}

function adminLogout() {
  localStorage.removeItem('currentUser');
  window.location.href = 'login.html';
}

// ====================== INIT ======================
document.addEventListener('DOMContentLoaded', async () => {
  checkAdminAuth();
  document.querySelectorAll('.tab-btn').forEach(btn => {
    btn.addEventListener('click', () => switchTab(btn.dataset.tab));
  });
  await loadTab('students');
  await loadBaseData();
});

async function loadBaseData() {
  try {
    const [students, courses, semesters] = await Promise.all([
      apiFetch(`${ADMIN_API}/students`),
      apiFetch(`${ADMIN_API}/courses`),
      apiFetch(`${ADMIN_API}/semesters`)
    ]);
    S.students = students;
    S.courses  = courses;
    S.semesters = semesters;
    populateGradeStudentSelect();
  } catch (e) {
    showToast('Lỗi tải dữ liệu nền: ' + e.message, 'error');
  }
}

function populateGradeStudentSelect() {
  const sel = document.getElementById('gradeStudentSelect');
  sel.innerHTML = '<option value="">— Chọn sinh viên —</option>';
  S.students.forEach(s => {
    const opt = document.createElement('option');
    opt.value = s.id;
    opt.textContent = `${s.studentCode} — ${s.fullName}`;
    sel.appendChild(opt);
  });
}

// ====================== TABS ======================
async function switchTab(name) {
  document.querySelectorAll('.tab-btn').forEach(b => b.classList.toggle('active', b.dataset.tab === name));
  document.querySelectorAll('.tab-panel').forEach(p => p.classList.toggle('active', p.id === `tab-${name}`));
  S.tab = name;
  await loadTab(name);
}

async function loadTab(name) {
  try {
    switch (name) {
      case 'students':
        S.students = await apiFetch(`${ADMIN_API}/students`);
        renderStudents();
        populateGradeStudentSelect();
        break;
      case 'courses':
        S.courses = await apiFetch(`${ADMIN_API}/courses`);
        renderCourses();
        break;
      case 'semesters':
        S.semesters = await apiFetch(`${ADMIN_API}/semesters`);
        renderSemesters();
        break;
      case 'sections':
        [S.courses, S.semesters, S.sections] = await Promise.all([
          apiFetch(`${ADMIN_API}/courses`),
          apiFetch(`${ADMIN_API}/semesters`),
          apiFetch(`${ADMIN_API}/course-sections`)
        ]);
        renderSections();
        break;
      case 'grades':
        break;
      case 'periods':
        [S.semesters, S.periods] = await Promise.all([
          apiFetch(`${ADMIN_API}/semesters`),
          apiFetch(`${ADMIN_API}/registration-periods`)
        ]);
        renderPeriods();
        break;
    }
  } catch (e) {
    showToast('Lỗi tải dữ liệu: ' + e.message, 'error');
  }
}

// ====================== RENDER TABLES ======================
function renderStudents() {
  const tbody = document.getElementById('tb-students');
  if (!S.students.length) {
    tbody.innerHTML = '<tr><td colspan="6" class="text-empty">Chưa có sinh viên nào.</td></tr>';
    return;
  }
  tbody.innerHTML = S.students.map((s, i) => `
    <tr>
      <td>${i + 1}</td>
      <td><strong class="font-mono">${s.studentCode}</strong></td>
      <td>${s.fullName}</td>
      <td style="color:var(--ink-muted);font-size:12px">${s.email || '—'}</td>
      <td>${s.className || '—'}</td>
      <td>
        <button class="btn-edit" onclick='openEdit("students", ${JSON.stringify(s)})'>Sửa</button>
        <button class="btn-del"  onclick="deleteItem('students','${s.id}','${escJs(s.fullName)}')">Xóa</button>
      </td>
    </tr>`).join('');
}

function renderCourses() {
  const tbody = document.getElementById('tb-courses');
  if (!S.courses.length) {
    tbody.innerHTML = '<tr><td colspan="6" class="text-empty">Chưa có môn học nào.</td></tr>';
    return;
  }
  tbody.innerHTML = S.courses.map((c, i) => `
    <tr>
      <td>${i + 1}</td>
      <td><strong class="font-mono">${c.code}</strong></td>
      <td>${c.name}</td>
      <td style="text-align:center">${c.credits}</td>
      <td style="color:var(--ink-muted);font-size:12px">${c.description || '—'}</td>
      <td>
        <button class="btn-edit" onclick='openEdit("courses", ${JSON.stringify(c)})'>Sửa</button>
        <button class="btn-del"  onclick="deleteItem('courses','${c.id}','${escJs(c.name)}')">Xóa</button>
      </td>
    </tr>`).join('');
}

function renderSemesters() {
  const tbody = document.getElementById('tb-semesters');
  if (!S.semesters.length) {
    tbody.innerHTML = '<tr><td colspan="7" class="text-empty">Chưa có học kỳ nào.</td></tr>';
    return;
  }
  tbody.innerHTML = S.semesters.map((s, i) => `
    <tr>
      <td>${i + 1}</td>
      <td><strong class="font-mono">${s.code}</strong></td>
      <td>${s.name}</td>
      <td>${s.academicYear || '—'}</td>
      <td>${s.startDate || '—'}</td>
      <td>${s.endDate || '—'}</td>
      <td>
        <button class="btn-edit" onclick='openEdit("semesters", ${JSON.stringify(s)})'>Sửa</button>
        <button class="btn-del"  onclick="deleteItem('semesters','${s.id}','${escJs(s.name)}')">Xóa</button>
      </td>
    </tr>`).join('');
}

function renderSections() {
  const tbody = document.getElementById('tb-sections');
  if (!S.sections.length) {
    tbody.innerHTML = '<tr><td colspan="8" class="text-empty">Chưa có lớp học phần nào.</td></tr>';
    return;
  }
  tbody.innerHTML = S.sections.map((s, i) => {
    const isRetake = (s.classType || '').toLowerCase().includes('học lại');
    const typeClass = isRetake ? 'reg-type-RETAKE' : 'reg-type-NEW';
    const statusColors = { open: 'var(--green)', planned: 'var(--ink-muted)', closed: 'var(--accent)', canceled: '#888' };
    const statusLabels = { open: 'Đang mở', planned: 'Kế hoạch', closed: 'Đã đóng', canceled: 'Hủy' };
    return `
    <tr>
      <td>${i + 1}</td>
      <td><strong class="font-mono">${s.code}</strong></td>
      <td>
        <div>${s.courseName}</div>
        <small style="color:var(--ink-muted)">${s.courseCode} · ${s.credits} TC</small>
      </td>
      <td style="font-size:12px;color:var(--ink-muted)">${s.semesterName}</td>
      <td><span class="reg-type-badge ${typeClass}">${s.classType || 'theory'}</span></td>
      <td style="text-align:center">${s.currentStudents}/${s.maxStudents}</td>
      <td style="color:${statusColors[s.status] || '#888'};font-weight:600;font-size:12px">
        ${statusLabels[s.status] || s.status}
      </td>
      <td>
        <button class="btn-edit" onclick='openEdit("sections", ${JSON.stringify(s)})'>Sửa</button>
        <button class="btn-del"  onclick="deleteItem('sections','${s.id}','${escJs(s.code)}')">Xóa</button>
      </td>
    </tr>`;
  }).join('');
}

function renderGrades() {
  const tbody = document.getElementById('tb-grades');
  if (!S.grades.length) {
    tbody.innerHTML = '<tr><td colspan="8" class="text-empty">Sinh viên này chưa có điểm thành phần nào.</td></tr>';
    return;
  }
  tbody.innerHTML = S.grades.map((g, i) => `
    <tr>
      <td>${i + 1}</td>
      <td><strong class="font-mono">${g.courseCode}</strong></td>
      <td>${g.courseName}</td>
      <td class="font-mono" style="font-size:12px">${g.sectionCode}</td>
      <td>${g.componentName || g.componentCode || '—'}</td>
      <td style="text-align:center">${g.weightPercentage ?? '—'}%</td>
      <td style="text-align:center;font-weight:700;color:${(g.score ?? 0) >= 5 ? 'var(--green)' : 'var(--accent)'}">
        ${g.score ?? '—'}
      </td>
      <td>
        <button class="btn-edit" onclick='openEdit("grades", ${JSON.stringify(g)})'>Sửa</button>
      </td>
    </tr>`).join('');
}

function renderPeriods() {
  const tbody = document.getElementById('tb-periods');
  if (!S.periods.length) {
    tbody.innerHTML = '<tr><td colspan="8" class="text-empty">Chưa có đợt đăng ký nào.</td></tr>';
    return;
  }
  tbody.innerHTML = S.periods.map((p, i) => {
    const start = p.startTime ? new Date(p.startTime).toLocaleString('vi-VN') : '—';
    const end   = p.endTime   ? new Date(p.endTime).toLocaleString('vi-VN')   : '—';
    return `
    <tr>
      <td>${i + 1}</td>
      <td><strong>${p.name}</strong></td>
      <td style="font-size:12px;color:var(--ink-muted)">${p.semesterName}</td>
      <td style="font-size:12px">${start}</td>
      <td style="font-size:12px">${end}</td>
      <td style="text-align:center;font-size:12px">${p.minCredits}/${p.maxCredits}</td>
      <td>
        <span class="status-pill ${p.isOpen ? 'status-pill-PENDING' : 'status-pill-CANCELED'}">
          ${p.isOpen ? 'Đang mở' : 'Đã đóng'}
        </span>
      </td>
      <td>
        <button class="btn-edit" onclick='openEdit("periods", ${JSON.stringify(p)})'>Sửa</button>
        <button class="btn-del"  onclick="deleteItem('periods','${p.id}','${escJs(p.name)}')">Xóa</button>
      </td>
    </tr>`;
  }).join('');
}

// ====================== LOAD GRADES ======================
async function loadGrades() {
  const studentId = document.getElementById('gradeStudentSelect').value;
  if (!studentId) { showToast('Vui lòng chọn sinh viên', 'error'); return; }
  try {
    S.grades = await apiFetch(`${ADMIN_API}/grade-components?studentId=${studentId}`);
    renderGrades();
    showToast(`Đã tải ${S.grades.length} điểm thành phần`, 'success');
  } catch (e) {
    showToast('Lỗi tải điểm: ' + e.message, 'error');
  }
}

// ====================== MODAL FIELD DEFINITIONS ======================
const FIELDS = {
  students: [
    { key: 'studentCode', label: 'Mã sinh viên', type: 'text', required: true, placeholder: 'VD: SV001' },
    { key: 'fullName',    label: 'Họ và tên',    type: 'text', required: true },
    { key: 'email',       label: 'Email',         type: 'email', placeholder: 'example@email.com' },
    { key: 'className',   label: 'Lớp',           type: 'text', placeholder: 'VD: CNTT01' },
  ],
  courses: [
    { key: 'code',        label: 'Mã môn học',   type: 'text', required: true, placeholder: 'VD: CS101' },
    { key: 'name',        label: 'Tên môn học',  type: 'text', required: true },
    { key: 'credits',     label: 'Số tín chỉ',   type: 'number', required: true, min: 1, max: 10 },
    { key: 'description', label: 'Mô tả',        type: 'textarea', fullWidth: true },
  ],
  semesters: [
    { key: 'code',         label: 'Mã học kỳ',    type: 'text', required: true, placeholder: 'VD: HK1_2425' },
    { key: 'name',         label: 'Tên học kỳ',   type: 'text', required: true },
    { key: 'academicYear', label: 'Năm học',       type: 'text', placeholder: 'VD: 2024-2025' },
    { key: 'startDate',    label: 'Ngày bắt đầu', type: 'date' },
    { key: 'endDate',      label: 'Ngày kết thúc',type: 'date' },
  ],
  sections: [
    { key: 'code',      label: 'Mã lớp HP',     type: 'text', required: true, placeholder: 'VD: CS101.01' },
    { key: 'name',      label: 'Tên lớp HP',    type: 'text' },
    { key: 'courseId',  label: 'Môn học',        type: 'select-courses',  required: true },
    { key: 'semesterId',label: 'Học kỳ',         type: 'select-semesters',required: true },
    { key: 'classType', label: 'Loại lớp',       type: 'select', options: [
      { value: 'theory',      label: 'Lý thuyết' },
      { value: 'lab',         label: 'Thực hành' },
      { value: 'Lớp học lại', label: 'Học lại' },
    ]},
    { key: 'maxStudents',        label: 'Sĩ số tối đa',   type: 'number', min: 1, max: 500 },
    { key: 'status',             label: 'Trạng thái',      type: 'select', options: [
      { value: 'open',     label: 'Đang mở' },
      { value: 'planned',  label: 'Lên kế hoạch' },
      { value: 'closed',   label: 'Đã đóng' },
      { value: 'canceled', label: 'Hủy' },
    ]},
    { key: 'registrationStart', label: 'Mở đăng ký',   type: 'datetime-local' },
    { key: 'registrationEnd',   label: 'Đóng đăng ký', type: 'datetime-local' },
  ],
  periods: [
    { key: 'name',        label: 'Tên đợt đăng ký', type: 'text',   required: true, fullWidth: true },
    { key: 'semesterId',  label: 'Học kỳ',           type: 'select-semesters', required: true },
    { key: 'startTime',   label: 'Thời gian bắt đầu',type: 'datetime-local', required: true },
    { key: 'endTime',     label: 'Thời gian kết thúc',type: 'datetime-local', required: true },
    { key: 'maxCredits',  label: 'TC tối đa',         type: 'number', min: 0, max: 50 },
    { key: 'minCredits',  label: 'TC tối thiểu',      type: 'number', min: 0, max: 50 },
    { key: 'allowRetake', label: 'Cho phép học lại',  type: 'select-bool' },
    { key: 'isOpen',      label: 'Trạng thái',        type: 'select-bool', options: [
      { value: 'true',  label: 'Đang mở' },
      { value: 'false', label: 'Đã đóng' },
    ]},
  ],
  grades: [
    { key: 'componentName',    label: 'Tên thành phần', type: 'text' },
    { key: 'weightPercentage', label: 'Trọng số (%)',   type: 'number', min: 0, max: 100, step: '0.01' },
    { key: 'score',            label: 'Điểm số',        type: 'number', min: 0, max: 10,  step: '0.01' },
  ],
};

// ====================== MODAL ======================
function openCreate(entity) {
  S.editEntity = entity;
  S.editId = null;
  const titles = {
    students: 'Thêm sinh viên mới',
    courses:  'Thêm môn học mới',
    semesters:'Thêm học kỳ mới',
    sections: 'Thêm lớp học phần mới',
    periods:  'Thêm đợt đăng ký mới',
  };
  document.getElementById('adminModalTitle').textContent = titles[entity] || 'Thêm mới';
  document.getElementById('adminModalBody').innerHTML = buildForm(entity, null);
  document.getElementById('adminModal').classList.add('show');
}

function openEdit(entity, data) {
  S.editEntity = entity;
  S.editId = data.id;
  const titles = {
    students: 'Sửa thông tin sinh viên',
    courses:  'Sửa môn học',
    semesters:'Sửa học kỳ',
    sections: 'Sửa lớp học phần',
    periods:  'Sửa đợt đăng ký',
    grades:   'Chỉnh sửa điểm thành phần',
  };
  document.getElementById('adminModalTitle').textContent = titles[entity] || 'Chỉnh sửa';
  document.getElementById('adminModalBody').innerHTML = buildForm(entity, data);
  document.getElementById('adminModal').classList.add('show');
}

function closeAdminModal() {
  document.getElementById('adminModal').classList.remove('show');
  S.editEntity = null;
  S.editId = null;
}

function buildForm(entity, data) {
  const fields = FIELDS[entity];
  if (!fields) return '<p>Không có trường nào được cấu hình.</p>';

  const rows = fields.map(f => {
    const val = data ? (data[f.key] ?? '') : '';
    const inputId = `field_${f.key}`;
    const required = f.required ? 'required' : '';
    const fullWidth = f.fullWidth ? 'form-full' : '';

    let input = '';
    if (f.type === 'textarea') {
      input = `<textarea id="${inputId}" class="form-control" rows="3" ${required}>${val}</textarea>`;
    } else if (f.type === 'select') {
      const opts = f.options.map(o =>
        `<option value="${o.value}" ${String(val) === String(o.value) ? 'selected' : ''}>${o.label}</option>`
      ).join('');
      input = `<select id="${inputId}" class="form-control" ${required}>${opts}</select>`;
    } else if (f.type === 'select-bool') {
      const boolOpts = f.options || [{ value: 'true', label: 'Có' }, { value: 'false', label: 'Không' }];
      const opts = boolOpts.map(o =>
        `<option value="${o.value}" ${String(val) === String(o.value) ? 'selected' : ''}>${o.label}</option>`
      ).join('');
      input = `<select id="${inputId}" class="form-control" ${required}>${opts}</select>`;
    } else if (f.type === 'select-courses') {
      const opts = S.courses.map(c =>
        `<option value="${c.id}" ${val === c.id ? 'selected' : ''}>${c.code} — ${c.name}</option>`
      ).join('');
      input = `<select id="${inputId}" class="form-control" ${required}><option value="">— Chọn môn học —</option>${opts}</select>`;
    } else if (f.type === 'select-semesters') {
      const opts = S.semesters.map(s =>
        `<option value="${s.id}" ${val === s.id ? 'selected' : ''}>${s.name}</option>`
      ).join('');
      input = `<select id="${inputId}" class="form-control" ${required}><option value="">— Chọn học kỳ —</option>${opts}</select>`;
    } else if (f.type === 'datetime-local') {
      const dtVal = val ? String(val).replace(' ', 'T').substring(0, 16) : '';
      input = `<input id="${inputId}" type="datetime-local" class="form-control" value="${dtVal}" ${required}>`;
    } else {
      const extraAttrs = [
        f.min  != null ? `min="${f.min}"` : '',
        f.max  != null ? `max="${f.max}"` : '',
        f.step != null ? `step="${f.step}"` : '',
        f.placeholder ? `placeholder="${f.placeholder}"` : '',
      ].filter(Boolean).join(' ');
      input = `<input id="${inputId}" type="${f.type}" class="form-control" value="${escHtml(String(val))}" ${extraAttrs} ${required}>`;
    }

    return `<div class="form-group ${fullWidth}">
      <label for="${inputId}">${f.label}${f.required ? ' <span class="req">*</span>' : ''}</label>
      ${input}
    </div>`;
  }).join('');

  return `<div class="form-grid">${rows}</div>`;
}

async function saveModal() {
  const entity = S.editEntity;
  const id = S.editId;
  if (!entity) return;

  const fields = FIELDS[entity];
  const body = {};
  for (const f of fields) {
    const el = document.getElementById(`field_${f.key}`);
    if (!el) continue;
    let val = el.value.trim();
    if (f.required && !val) {
      showToast(`Vui lòng điền ${f.label}`, 'error');
      el.focus();
      return;
    }
    if (val === '') { body[f.key] = null; continue; }
    if (f.type === 'number') { body[f.key] = parseFloat(val); continue; }
    if (f.type === 'select-bool') { body[f.key] = val === 'true'; continue; }
    body[f.key] = val;
  }

  const saveBtn = document.getElementById('adminModalSaveBtn');
  saveBtn.disabled = true;
  saveBtn.textContent = 'Đang lưu...';

  try {
    if (id) {
      await apiFetch(`${ADMIN_API}/${entityPath(entity)}/${id}`, {
        method: 'PUT', body: JSON.stringify(body)
      });
      showToast('Cập nhật thành công!', 'success');
    } else {
      await apiFetch(`${ADMIN_API}/${entityPath(entity)}`, {
        method: 'POST', body: JSON.stringify(body)
      });
      showToast('Thêm mới thành công!', 'success');
    }
    closeAdminModal();
    await loadTab(entity === 'grades' ? 'grades' : entity);
    if (entity === 'grades') {
      await loadGrades();
    }
  } catch (e) {
    showToast('Lỗi: ' + e.message, 'error');
  } finally {
    saveBtn.disabled = false;
    saveBtn.textContent = 'Lưu';
  }
}

function entityPath(entity) {
  const map = {
    students: 'students',
    courses:  'courses',
    semesters:'semesters',
    sections: 'course-sections',
    periods:  'registration-periods',
    grades:   'grade-components',
  };
  return map[entity] || entity;
}

// ====================== DELETE ======================
async function deleteItem(entity, id, label) {
  if (!confirm(`Xóa "${label}"?\nDữ liệu sẽ bị ẩn khỏi hệ thống (soft delete).`)) return;
  try {
    await apiFetch(`${ADMIN_API}/${entityPath(entity)}/${id}`, { method: 'DELETE' });
    showToast('Đã xóa thành công', 'success');
    await loadTab(entity);
  } catch (e) {
    showToast('Xóa thất bại: ' + e.message, 'error');
  }
}

// ====================== UTILS ======================
function showToast(msg, type = 'success') {
  const t = document.getElementById('toast');
  t.textContent = msg;
  t.className = `toast show ${type}`;
  setTimeout(() => t.classList.remove('show'), 3500);
}

function escHtml(str) {
  return String(str)
    .replace(/&/g, '&amp;')
    .replace(/"/g, '&quot;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;');
}

function escJs(str) {
  return String(str || '').replace(/'/g, "\\'").replace(/"/g, '\\"');
}
