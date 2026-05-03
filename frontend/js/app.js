/* =====================================================
   APP LOGIC - Đăng ký học lại
   ===================================================== */

const State = {
  students: [],
  periods: [],
  currentStudentId: null,
  currentPeriodId: null,
  retakeCourses: [],
  registrations: [],
  courseSections: [],
  sectionFilter: ''
};

// ====================== INIT ======================
document.addEventListener('DOMContentLoaded', async () => {
  bindEvents();
  await loadInitialData();
});

function bindEvents() {
  document.getElementById('loadBtn').addEventListener('click', onLoadData);
  document.getElementById('studentSelect').addEventListener('change', e => {
    State.currentStudentId = e.target.value || null;
  });
  document.getElementById('periodSelect').addEventListener('change', e => {
    State.currentPeriodId = e.target.value || null;
  });
  document.getElementById('modalCancel').addEventListener('click', closeModal);
  document.getElementById('confirmModal').addEventListener('click', e => {
    if (e.target.id === 'confirmModal') closeModal();
  });
  document.getElementById('sectionSearch').addEventListener('input', e => {
    State.sectionFilter = e.target.value.toLowerCase();
    renderCourseSectionList();
  });
}

async function loadInitialData() {
  try {
    const [students, periods] = await Promise.all([
      Api.getStudents(),
      Api.getOpenPeriods()
    ]);

    State.students = students;
    State.periods = periods;

    // Populate student dropdown
    const studentSelect = document.getElementById('studentSelect');
    students.forEach(s => {
      const opt = document.createElement('option');
      opt.value = s.id;
      opt.textContent = `${s.studentCode} — ${s.fullName} (${s.className})`;
      studentSelect.appendChild(opt);
    });

    // Populate period dropdown
    const periodSelect = document.getElementById('periodSelect');
    periods.forEach(p => {
      const opt = document.createElement('option');
      opt.value = p.id;
      opt.textContent = p.name;
      periodSelect.appendChild(opt);
    });

    // Update status badge
    document.getElementById('statStatus').textContent =
      periods.length > 0 ? 'Đang mở' : 'Đã đóng';
  } catch (err) {
    showToast('Không kết nối được server: ' + err.message, 'error');
  }
}

// ====================== LOAD DATA ======================
async function onLoadData() {
  if (!State.currentStudentId) {
    showToast('Vui lòng chọn sinh viên', 'error');
    return;
  }
  if (!State.currentPeriodId) {
    showToast('Vui lòng chọn đợt đăng ký', 'error');
    return;
  }

  const btn = document.getElementById('loadBtn');
  btn.disabled = true;
  btn.querySelector('span').textContent = 'Đang tải...';

  try {
    const [retakes, registrations, sections] = await Promise.all([
      Api.getRetakeCourses(State.currentStudentId),
      Api.getStudentRegistrations(State.currentStudentId),
      Api.getCourseSections()
    ]);

    State.retakeCourses = retakes;
    State.registrations = registrations;
    State.courseSections = sections;

    renderRetakeList();
    renderCourseSectionList();
    renderHistory();
    updateStats();

    showToast(`Đã tải ${retakes.length} môn học lại · ${sections.length} lớp học phần`, 'success');
  } catch (err) {
    showToast('Lỗi: ' + err.message, 'error');
  } finally {
    btn.disabled = false;
    btn.querySelector('span').textContent = 'Tải dữ liệu';
  }
}

// ====================== RENDER RETAKE LIST ======================
function renderRetakeList() {
  const wrap = document.getElementById('retakeList');

  if (State.retakeCourses.length === 0) {
    wrap.innerHTML = `
      <div class="empty-state">
        <div class="empty-icon">✓</div>
        <p>Sinh viên không có môn nào cần học lại. Tuyệt vời!</p>
      </div>`;
    return;
  }

  wrap.innerHTML = State.retakeCourses.map((c, idx) => renderRetakeCard(c, idx)).join('');

  // Bind expand/collapse
  wrap.querySelectorAll('.retake-card-head').forEach(head => {
    head.addEventListener('click', () => {
      head.parentElement.classList.toggle('expanded');
    });
  });

  // Bind register buttons
  wrap.querySelectorAll('.btn-register').forEach(btn => {
    btn.addEventListener('click', e => {
      e.stopPropagation();
      const sectionId = btn.dataset.sectionId;
      const sectionName = btn.dataset.sectionName;
      confirmRegister(sectionId, sectionName);
    });
  });
}

function renderRetakeCard(course, idx) {
  const statusClass = `status-${course.gradeStatus.toLowerCase()}`;
  const statusText = {
    FAILED: 'Trượt',
    PASSED: 'Đậu',
    IMPROVABLE: 'Có thể cải thiện'
  }[course.gradeStatus] || course.gradeStatus;

  const grades = (course.gradeComponents || []).map(g => `
    <div class="grade-pill">
      <div>
        <div class="name">${g.componentName || '—'}</div>
        <div class="name-sub">${g.componentCode} · ${g.weightPercentage}%</div>
      </div>
      <div class="score">${g.score}</div>
    </div>
  `).join('');

  const sections = (course.availableSections || []).length > 0
    ? course.availableSections.map(s => renderSectionRow(s)).join('')
    : `<div class="no-sections">Hiện chưa có lớp học phần đang mở cho môn này.</div>`;

  return `
    <article class="retake-card" data-idx="${idx}">
      <div class="retake-card-head">
        <div class="course-code-block">
          <span class="code">${course.courseCode}</span>
          <span class="credits">${course.credits} TC</span>
        </div>
        <div class="course-info">
          <h3>${course.courseName}</h3>
          <div class="course-meta">
            <span>Học kỳ: ${course.semesterName || '—'}</span>
            <span class="status-badge ${statusClass}">${statusText}</span>
          </div>
        </div>
        <div class="score-block">
          <div class="score-value">${course.totalScore}</div>
          <div class="score-label">điểm tổng kết</div>
        </div>
        <div class="expand-icon">▼</div>
      </div>

      <div class="retake-card-body">
        <div class="body-section">
          <h4>Chi tiết điểm thành phần</h4>
          <div class="grade-grid">${grades}</div>
        </div>

        <div class="body-section">
          <h4>Lớp học phần đang mở (${course.availableSections.length})</h4>
          <div class="sections-list">${sections}</div>
        </div>
      </div>
    </article>
  `;
}

function renderSectionRow(s) {
  const isRetake = (s.classType || '').toLowerCase().includes('học lại');
  const typeTag = isRetake ? 'retake' : '';
  const typeLabel = s.classType || 'theory';

  return `
    <div class="section-row">
      <div class="section-code-tag">${s.sectionCode}</div>
      <div class="section-name">
        ${s.sectionName || s.sectionCode}
        <small>${s.semesterName || ''}</small>
      </div>
      <div class="section-type-tag ${typeTag}">${typeLabel}</div>
      <div class="section-slots">
        <div class="num">${s.currentStudents}/${s.maxStudents}</div>
        <div class="label">còn ${s.remainingSlots}</div>
      </div>
      <button class="btn-register"
              data-section-id="${s.sectionId}"
              data-section-name="${s.sectionCode} - ${s.sectionName || ''}"
              ${s.remainingSlots <= 0 ? 'disabled' : ''}>
        ${s.remainingSlots <= 0 ? 'Đã đầy' : 'Đăng ký'}
      </button>
    </div>
  `;
}

// ====================== REGISTER ======================
function confirmRegister(sectionId, sectionName) {
  document.getElementById('modalTitle').textContent = 'Xác nhận đăng ký học lại';
  document.getElementById('modalMsg').textContent =
    `Bạn có chắc muốn đăng ký lớp "${sectionName}" theo dạng HỌC LẠI?`;

  const okBtn = document.getElementById('modalOk');
  // Reset listeners
  const newOk = okBtn.cloneNode(true);
  okBtn.parentNode.replaceChild(newOk, okBtn);

  newOk.addEventListener('click', async () => {
    closeModal();
    await doRegister(sectionId);
  });

  openModal();
}

async function doRegister(sectionId) {
  try {
    await Api.register({
      studentId: State.currentStudentId,
      courseSectionId: sectionId,
      registrationPeriodId: State.currentPeriodId,
      registrationType: 'RETAKE',
      note: 'Đăng ký học lại từ giao diện sinh viên'
    });
    showToast('Đăng ký thành công!', 'success');
    // Refresh
    await onLoadData();
  } catch (err) {
    showToast('Đăng ký thất bại: ' + err.message, 'error');
  }
}

async function doCancel(registrationId) {
  if (!confirm('Bạn có chắc muốn hủy đăng ký này?')) return;
  try {
    await Api.cancelRegistration(registrationId);
    showToast('Hủy đăng ký thành công', 'success');
    await onLoadData();
  } catch (err) {
    showToast('Hủy thất bại: ' + err.message, 'error');
  }
}

// ====================== COURSE SECTIONS ======================
function renderCourseSectionList() {
  const tbody = document.getElementById('courseSectionTableBody');

  if (State.courseSections.length === 0) {
    tbody.innerHTML = '<tr><td colspan="9" class="text-empty">Không có lớp học phần nào đang mở.</td></tr>';
    return;
  }

  const keyword = State.sectionFilter;
  const filtered = keyword
    ? State.courseSections.filter(s =>
        (s.sectionCode || '').toLowerCase().includes(keyword) ||
        (s.courseName || '').toLowerCase().includes(keyword) ||
        (s.courseCode || '').toLowerCase().includes(keyword)
      )
    : State.courseSections;

  if (filtered.length === 0) {
    tbody.innerHTML = '<tr><td colspan="9" class="text-empty">Không tìm thấy lớp học phần phù hợp.</td></tr>';
    return;
  }

  const registeredSectionIds = new Set(
    State.registrations
      .filter(r => r.status !== 'CANCELED')
      .map(r => r.courseSectionId)
  );

  tbody.innerHTML = filtered.map((s, i) => {
    const isRetake = (s.classType || '').toLowerCase().includes('học lại');
    const typeClass = isRetake ? 'reg-type-RETAKE' : 'reg-type-NEW';
    const typeLabel = isRetake ? 'Học lại' : (s.classType || 'Lý thuyết');
    const alreadyRegistered = registeredSectionIds.has(s.sectionId);
    const isFull = s.remainingSlots <= 0;

    return `
      <tr>
        <td>${i + 1}</td>
        <td><strong class="font-mono">${s.sectionCode}</strong></td>
        <td>
          <div>${s.courseName}</div>
          <small style="color:var(--ink-muted)">${s.courseCode}</small>
        </td>
        <td style="text-align:center">${s.credits}</td>
        <td><span class="reg-type-badge ${typeClass}">${typeLabel}</span></td>
        <td style="color:var(--ink-muted);font-size:12px">${s.semesterName || '—'}</td>
        <td style="text-align:center">${s.currentStudents}/${s.maxStudents}</td>
        <td style="text-align:center;font-weight:600;color:${s.remainingSlots > 0 ? 'var(--green)' : 'var(--accent)'}">
          ${s.remainingSlots}
        </td>
        <td>
          ${alreadyRegistered
            ? '<span style="color:var(--green);font-size:12px;font-weight:600">✓ Đã đăng ký</span>'
            : `<button class="btn-register-new" ${isFull ? 'disabled' : ''}
                 onclick="confirmRegisterNew('${s.sectionId}', '${s.sectionCode} - ${(s.sectionName || '').replace(/'/g, '')}', '${isRetake ? 'RETAKE' : 'NEW'}')">
                 ${isFull ? 'Đã đầy' : 'Đăng ký'}
               </button>`
          }
        </td>
      </tr>`;
  }).join('');
}

function confirmRegisterNew(sectionId, sectionName, regType) {
  if (!State.currentStudentId) { showToast('Vui lòng chọn sinh viên trước', 'error'); return; }
  if (!State.currentPeriodId)  { showToast('Vui lòng chọn đợt đăng ký trước', 'error'); return; }

  const typeLabel = regType === 'RETAKE' ? 'HỌC LẠI' : 'MỚI';
  document.getElementById('modalTitle').textContent = 'Xác nhận đăng ký học phần';
  document.getElementById('modalMsg').textContent =
    `Đăng ký lớp "${sectionName}" (${typeLabel})?`;

  const okBtn = document.getElementById('modalOk');
  const newOk = okBtn.cloneNode(true);
  okBtn.parentNode.replaceChild(newOk, okBtn);
  newOk.addEventListener('click', async () => {
    closeModal();
    try {
      await Api.register({
        studentId: State.currentStudentId,
        courseSectionId: sectionId,
        registrationPeriodId: State.currentPeriodId,
        registrationType: regType,
        note: 'Đăng ký từ giao diện sinh viên'
      });
      showToast('Đăng ký thành công!', 'success');
      await onLoadData();
    } catch (err) {
      showToast('Đăng ký thất bại: ' + err.message, 'error');
    }
  });

  openModal();
}

// ====================== HISTORY ======================
function renderHistory() {
  const tbody = document.getElementById('historyTableBody');
  if (State.registrations.length === 0) {
    tbody.innerHTML = '<tr><td colspan="8" class="text-empty">Chưa có đăng ký nào</td></tr>';
    return;
  }

  tbody.innerHTML = State.registrations.map((r, i) => {
    const date = r.registeredAt ? new Date(r.registeredAt).toLocaleString('vi-VN') : '—';
    return `
      <tr>
        <td>${i + 1}</td>
        <td><strong>${r.sectionCode}</strong></td>
        <td>${r.courseName || '—'}</td>
        <td>${r.credits || 0}</td>
        <td><span class="reg-type-badge reg-type-${r.registrationType}">${r.registrationType}</span></td>
        <td><span class="status-pill status-pill-${r.status}">${r.status}</span></td>
        <td>${date}</td>
        <td>
          ${r.status !== 'CANCELED'
            ? `<button class="btn-cancel-small" onclick="doCancel('${r.id}')">Hủy</button>`
            : ''}
        </td>
      </tr>`;
  }).join('');
}

// ====================== STATS ======================
function updateStats() {
  document.getElementById('statFailed').textContent = State.retakeCourses.length;

  const totalCredits = State.retakeCourses.reduce((s, c) => s + (c.credits || 0), 0);
  document.getElementById('statCredits').textContent = totalCredits;

  const activeRegs = State.registrations.filter(r => r.status !== 'CANCELED').length;
  document.getElementById('statRegistered').textContent = activeRegs;
}

// ====================== UI HELPERS ======================
function showToast(msg, type = 'success') {
  const toast = document.getElementById('toast');
  toast.textContent = msg;
  toast.className = `toast show ${type}`;
  setTimeout(() => toast.classList.remove('show'), 3000);
}

function openModal() {
  document.getElementById('confirmModal').classList.add('show');
}

function closeModal() {
  document.getElementById('confirmModal').classList.remove('show');
}
