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

  // Group components by scsId (one section per group)
  const groups = new Map();
  S.grades.forEach(g => {
    const key = g.scsId || (g.courseCode + '_' + g.sectionCode);
    if (!groups.has(key)) {
      groups.set(key, { scsId: g.scsId, courseCode: g.courseCode, courseName: g.courseName, sectionCode: g.sectionCode, components: [] });
    }
    groups.get(key).components.push(g);
  });

  const clsColor = { FAILED: 'var(--accent)', IMPROVABLE: 'var(--amber)', PASSED: 'var(--green)' };
  const clsBg    = { FAILED: 'var(--accent-soft)', IMPROVABLE: 'var(--amber-soft)', PASSED: 'var(--green-soft)' };
  const clsLabel = { FAILED: 'Cần học lại', IMPROVABLE: 'Cải thiện', PASSED: 'Đạt' };

  let html = '';
  groups.forEach(group => {
    let total = 0;
    group.components.forEach(c => { total += (parseFloat(c.score) || 0) * (parseFloat(c.weightPercentage) || 0) / 100; });
    const cls = total < 5 ? 'FAILED' : total < 7 ? 'IMPROVABLE' : 'PASSED';

    // Section summary row
    html += `
    <tr style="background:#f1f5f9">
      <td colspan="3" style="padding:10px 12px;font-weight:700;border-left:3px solid ${clsColor[cls]}">
        <strong class="font-mono">${group.courseCode}</strong>
        <span style="color:var(--ink-muted);margin:0 6px">—</span>${group.courseName}
      </td>
      <td class="font-mono" style="font-size:12px;color:var(--ink-muted)">${group.sectionCode || '—'}</td>
      <td style="text-align:right;font-size:11px;color:var(--ink-muted);padding-right:4px">Tổng kết:</td>
      <td style="text-align:center;font-weight:700;font-size:16px;color:${clsColor[cls]}">${total.toFixed(2)}</td>
      <td style="text-align:center">
        <span style="font-size:10px;font-weight:700;padding:2px 8px;border-radius:999px;background:${clsBg[cls]};color:${clsColor[cls]}">${clsLabel[cls]}</span>
      </td>
      <td style="text-align:center">
        ${group.scsId ? `<button class="btn-del" onclick="deleteGradesByScs('${group.scsId}')">Xóa</button>` : ''}
      </td>
    </tr>`;

    // Component detail rows
    group.components.forEach((c, idx) => {
      const scoreColor = (parseFloat(c.score) || 0) >= 5 ? 'var(--green)' : 'var(--accent)';
      html += `
      <tr>
        <td style="color:var(--ink-muted);text-align:center;font-size:12px">${idx + 1}</td>
        <td><strong class="font-mono" style="font-size:12px">${c.componentCode || '—'}</strong></td>
        <td>${c.componentName || '—'}</td>
        <td style="color:var(--ink-muted)">—</td>
        <td style="color:var(--ink-muted)">—</td>
        <td style="text-align:center">${c.weightPercentage ?? '—'}%</td>
        <td style="text-align:center;font-weight:700;color:${scoreColor}">${c.score ?? '—'}</td>
        <td>
          <button class="btn-edit" onclick='openEdit("grades", ${JSON.stringify(c)})'>Sửa</button>
        </td>
      </tr>`;
    });
  });

  tbody.innerHTML = html;
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
async function loadGrades(silent = false) {
  const studentId = document.getElementById('gradeStudentSelect').value;
  if (!studentId) { if (!silent) showToast('Vui lòng chọn sinh viên', 'error'); return; }
  try {
    [S.grades, S.sections] = await Promise.all([
      apiFetch(`${ADMIN_API}/grade-components?studentId=${studentId}`),
      apiFetch(`${ADMIN_API}/course-sections`)
    ]);
    renderGrades();
    if (!silent) showToast(`Đã tải ${S.grades.length} điểm thành phần`, 'success');
  } catch (e) {
    showToast('Lỗi tải điểm: ' + e.message, 'error');
  }
}

async function openAddGrade() {
  const studentId = document.getElementById('gradeStudentSelect').value;
  if (!studentId) { showToast('Vui lòng chọn sinh viên trước', 'error'); return; }

  if (!S.sections || !S.sections.length) {
    try {
      S.sections = await apiFetch(`${ADMIN_API}/course-sections`);
    } catch (e) {
      showToast('Lỗi tải danh sách lớp HP: ' + e.message, 'error');
      return;
    }
  }

  const sectionOptions = S.sections.map(s =>
    `<option value="${s.id}">${s.code} — ${s.courseName} (${s.semesterName || ''})</option>`
  ).join('');

  document.getElementById('adminModalTitle').textContent = 'Thêm điểm cho sinh viên';
  document.getElementById('adminModalBody').innerHTML = `
    <div style="margin-bottom:16px">
      <label style="font-size:12px;font-weight:700;color:var(--ink-muted);display:block;margin-bottom:6px">CHỌN LỚP HỌC PHẦN ĐÃ HỌC</label>
      <select id="gradeSectionId" class="form-control" required>
        <option value="">— Chọn lớp học phần —</option>
        ${sectionOptions}
      </select>
      <div style="font-size:11px;color:var(--ink-muted);margin-top:4px">Chọn lớp sinh viên đã học (kể cả lớp đã đóng)</div>
    </div>

    <div style="margin-bottom:12px;display:flex;justify-content:space-between;align-items:center">
      <label style="font-size:12px;font-weight:700;color:var(--ink-muted)">ĐIỂM THÀNH PHẦN</label>
      <button type="button" class="btn-add" style="padding:5px 12px;font-size:12px" onclick="addGradeRow()">+ Thêm dòng</button>
    </div>

    <div id="gradeRowsWrap">
      <div style="display:grid;grid-template-columns:1fr 1.5fr 80px 80px 32px;gap:8px;margin-bottom:6px;font-size:11px;font-weight:700;color:var(--ink-muted);padding:0 4px">
        <span>MÃ TP</span><span>TÊN THÀNH PHẦN</span><span>TRỌNG SỐ %</span><span>ĐIỂM</span><span></span>
      </div>
      <!-- rows added here -->
    </div>

    <div id="gradeScorePreview" style="margin-top:16px;padding:12px 16px;background:var(--line-soft);border-radius:10px;font-size:13px;display:none">
      Điểm tổng kết: <strong id="gradeScoreValue" style="font-size:18px;margin-left:8px"></strong>
      <span id="gradeScoreLabel" style="margin-left:8px;font-weight:600;padding:2px 10px;border-radius:999px;font-size:11px"></span>
    </div>
  `;

  // Thêm 3 dòng mặc định
  addGradeRow('QT', 'Quá trình', 40, '');
  addGradeRow('GK', 'Giữa kỳ',  20, '');
  addGradeRow('CK', 'Cuối kỳ',  40, '');

  // Bind preview khi nhập điểm
  document.getElementById('gradeRowsWrap').addEventListener('input', previewGradeScore);

  document.getElementById('adminModal').classList.add('show');
  S.editEntity = '__grade__';
}

function addGradeRow(code = '', name = '', weight = '', score = '') {
  const wrap = document.getElementById('gradeRowsWrap');
  const row = document.createElement('div');
  row.className = 'grade-input-row';
  row.style = 'display:grid;grid-template-columns:1fr 1.5fr 80px 80px 32px;gap:8px;margin-bottom:8px;align-items:center';
  row.innerHTML = `
    <input class="form-control grade-code"   value="${code}"   placeholder="QT" style="font-family:var(--font-mono);font-size:12px">
    <input class="form-control grade-name"   value="${name}"   placeholder="Quá trình">
    <input class="form-control grade-weight" value="${weight}" placeholder="40" type="number" min="0" max="100" style="text-align:center"
      oninput="let v=parseFloat(this.value);if(!isNaN(v)){if(v>100)this.value=100;if(v<0)this.value=0}">
    <input class="form-control grade-score"  value="${score}"  placeholder="0-10" type="number" min="0" max="10" step="0.1" style="text-align:center"
      oninput="let v=parseFloat(this.value);if(!isNaN(v)){if(v>10)this.value=10;if(v<0)this.value=0}">
    <button type="button" onclick="this.parentElement.remove();previewGradeScore()"
      style="width:28px;height:28px;border:none;background:var(--accent-soft);color:var(--accent);border-radius:6px;cursor:pointer;font-size:14px;display:flex;align-items:center;justify-content:center">×</button>
  `;
  wrap.appendChild(row);
}

function previewGradeScore() {
  const rows = document.querySelectorAll('.grade-input-row');
  let total = 0, totalWeight = 0;
  rows.forEach(row => {
    const w = parseFloat(row.querySelector('.grade-weight').value) || 0;
    const s = parseFloat(row.querySelector('.grade-score').value);
    if (!isNaN(s)) { total += s * w / 100; totalWeight += w; }
  });
  if (totalWeight === 0) return;
  const preview = document.getElementById('gradeScorePreview');
  const val = document.getElementById('gradeScoreValue');
  const lbl = document.getElementById('gradeScoreLabel');
  preview.style.display = 'block';
  val.textContent = total.toFixed(2);
  if (total < 5) {
    val.style.color = 'var(--accent)';
    lbl.textContent = 'Cần học lại'; lbl.style.background = 'var(--accent-soft)'; lbl.style.color = 'var(--accent)';
  } else if (total < 7) {
    val.style.color = 'var(--amber)';
    lbl.textContent = 'Có thể cải thiện'; lbl.style.background = 'var(--amber-soft)'; lbl.style.color = 'var(--amber)';
  } else {
    val.style.color = 'var(--green)';
    lbl.textContent = 'Đạt'; lbl.style.background = 'var(--green-soft)'; lbl.style.color = 'var(--green)';
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

async function saveGrade() {
  const studentId = document.getElementById('gradeStudentSelect').value;
  const sectionId = document.getElementById('gradeSectionId')?.value;
  if (!sectionId) { showToast('Vui lòng chọn lớp học phần', 'error'); return; }

  const rows = document.querySelectorAll('.grade-input-row');
  const components = [];
  let valid = true;
  rows.forEach(row => {
    const code   = row.querySelector('.grade-code').value.trim();
    const name   = row.querySelector('.grade-name').value.trim();
    const weight = parseFloat(row.querySelector('.grade-weight').value);
    const score  = parseFloat(row.querySelector('.grade-score').value);
    if (!code || isNaN(weight) || isNaN(score)) { valid = false; return; }
    components.push({ componentCode: code, componentName: name || code, weightPercentage: weight, score });
  });
  if (!valid) { showToast('Vui lòng điền đầy đủ mã, trọng số và điểm cho mỗi thành phần', 'error'); return; }
  if (!components.length) { showToast('Cần ít nhất 1 thành phần điểm', 'error'); return; }

  const saveBtn = document.getElementById('adminModalSaveBtn');
  saveBtn.disabled = true;
  saveBtn.textContent = 'Đang lưu...';

  try {
    const result = await apiFetch(`${ADMIN_API}/grade-components`, {
      method: 'POST',
      body: JSON.stringify({ studentId, courseSectionId: sectionId, components })
    });
    const labelMap = { FAILED: 'Cần học lại', IMPROVABLE: 'Cải thiện được', PASSED: 'Đạt' };
    showToast(`Đã lưu! Tổng kết: ${result.totalScore} — ${labelMap[result.status] || result.label}`, 'success');
    closeAdminModal();
    await loadGrades(true);
  } catch (e) {
    showToast('Lỗi lưu điểm: ' + e.message, 'error');
  } finally {
    saveBtn.disabled = false;
    saveBtn.textContent = 'Lưu';
  }
}

async function deleteGradesByScs(scsId) {
  if (!confirm('Xóa toàn bộ điểm của môn học này?\nThao tác không thể hoàn tác.')) return;
  try {
    await apiFetch(`${ADMIN_API}/grade-components/section/${scsId}`, { method: 'DELETE' });
    showToast('Đã xóa điểm môn học', 'success');
    await loadGrades(true);
  } catch (e) {
    showToast('Xóa thất bại: ' + e.message, 'error');
  }
}

async function saveModal() {
  const entity = S.editEntity;
  const id = S.editId;
  if (!entity) return;

  if (entity === '__grade__') {
    await saveGrade();
    return;
  }

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
      await loadGrades(true);
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
