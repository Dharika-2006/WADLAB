<script setup>
import { ref, onMounted } from "vue";

const API = "http://localhost:8080/api";

const employees = ref([]);
const leaves = ref([]);
const vacationEmployees = ref([]);

const empid = ref("");
const leaveType = ref("Vacation");
const requestedDays = ref(1);
const searchLeaveType = ref("Vacation");

const newEmployee = ref({
  empid: "",
  name: "",
  dept: "",
  leavebal: 10,
});

const message = ref("");
const error = ref("");

async function request(url, options = {}) {
  const response = await fetch(`${API}${url}`, {
    headers: { "Content-Type": "application/json" },
    ...options,
  });

  const text = await response.text();
  let data = null;

  if (text) {
    try {
      data = JSON.parse(text);
    } catch {
      data = text;
    }
  }

  if (!response.ok) {
    throw new Error(
      data?.message || data?.error || `Request failed: ${response.status}`,
    );
  }

  return data;
}

async function loadEmployees() {
  employees.value = await request("/employees");
}

async function loadLeaves() {
  leaves.value = await request("/leave");
}

async function loadVacationEmployees() {
  const type = encodeURIComponent(searchLeaveType.value);
  vacationEmployees.value = await request(`/employees/byleave/${type}`);
}

async function refreshAll() {
  error.value = "";

  try {
    await Promise.all([loadEmployees(), loadLeaves()]);
  } catch (e) {
    error.value = e.message;
  }
}

// Admin: add a new employee
async function addEmployee() {
  message.value = "";
  error.value = "";

  try {
    await request("/employees", {
      method: "POST",
      body: JSON.stringify({
        empid: newEmployee.value.empid,
        name: newEmployee.value.name,
        dept: newEmployee.value.dept,
        leavebal: Number(newEmployee.value.leavebal),
      }),
    });

    message.value = "Employee added successfully!";

    newEmployee.value = {
      empid: "",
      name: "",
      dept: "",
      leavebal: 10,
    };

    await loadEmployees();
  } catch (e) {
    error.value = e.message;
  }
}

// Employee: apply for leave
async function applyLeave() {
  message.value = "";
  error.value = "";

  try {
    const created = await request("/leave", {
      method: "POST",
      body: JSON.stringify({
        empid: empid.value,
        leaveType: leaveType.value,
        requestedDays: Number(requestedDays.value),
      }),
    });

    message.value = `Leave applied! Status: ${created.status}`;
    await loadLeaves();
  } catch (e) {
    error.value = e.message;
  }
}

// Admin: approve a pending leave
async function approveLeave(leaveId) {
  message.value = "";
  error.value = "";

  try {
    const updated = await request(`/leaves/${leaveId}/approve`, {
      method: "PUT",
    });

    message.value = `Leave ${updated.status.toLowerCase()} successfully.`;
    await refreshAll();
  } catch (e) {
    error.value = e.message;
  }
}

// Admin: reject a pending leave
async function rejectLeave(leaveId) {
  message.value = "";
  error.value = "";

  try {
    const updated = await request(`/leaves/${leaveId}/reject`, {
      method: "PUT",
    });

    message.value = `Leave ${updated.status.toLowerCase()} successfully.`;
    await loadLeaves();
  } catch (e) {
    error.value = e.message;
  }
}

// Find employees with the selected leave type
async function findEmployeesByLeave() {
  error.value = "";

  try {
    await loadVacationEmployees();
  } catch (e) {
    error.value = e.message;
  }
}

onMounted(refreshAll);
</script>

<template>
  <main class="container">
    <header>
      <div>
        <p class="eyebrow">ADMIN & EMPLOYEE PORTAL</p>
        <h1>Employee Leave Management</h1>
      </div>

      <button @click="refreshAll">Refresh data</button>
    </header>

    <p v-if="message" class="success">{{ message }}</p>
    <p v-if="error" class="error">{{ error }}</p>

    <!-- Admin: add employee -->
    <section class="panel admin-panel">
      <h2>Admin — Add Employee</h2>

      <form @submit.prevent="addEmployee">
        <label>
          Employee ID
          <input
            v-model.trim="newEmployee.empid"
            placeholder="e.g. E102"
            required
          />
        </label>

        <label>
          Employee name
          <input
            v-model.trim="newEmployee.name"
            placeholder="Enter name"
            required
          />
        </label>

        <label>
          Department
          <input
            v-model.trim="newEmployee.dept"
            placeholder="e.g. CSE"
            required
          />
        </label>

        <label>
          Initial leave balance
          <input
            v-model.number="newEmployee.leavebal"
            type="number"
            min="0"
            required
          />
        </label>

        <button type="submit">+ Add Employee</button>
      </form>
    </section>

    <!-- Employee: apply leave -->
    <section class="panel">
      <h2>Apply for Leave</h2>

      <form @submit.prevent="applyLeave">
        <label>
          Employee ID
          <input v-model.trim="empid" placeholder="e.g. E101" required />
        </label>

        <label>
          Leave type
          <select v-model="leaveType">
            <option>Vacation</option>
            <option>Casual</option>
            <option>Sick</option>
          </select>
        </label>

        <label>
          Requested days
          <input
            v-model.number="requestedDays"
            type="number"
            min="1"
            required
          />
        </label>

        <button type="submit">Submit leave</button>
      </form>
    </section>

    <!-- Employee list -->
    <section class="panel">
      <h2>Employees</h2>

      <table>
        <thead>
          <tr>
            <th>Employee ID</th>
            <th>Name</th>
            <th>Department</th>
            <th>Leave balance</th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="employee in employees" :key="employee.id">
            <td>{{ employee.empid }}</td>
            <td>{{ employee.name }}</td>
            <td>{{ employee.dept }}</td>
            <td>{{ employee.leavebal }}</td>
          </tr>

          <tr v-if="employees.length === 0">
            <td colspan="4">No employees found.</td>
          </tr>
        </tbody>
      </table>
    </section>

    <!-- Leave applications and admin actions -->
    <section class="panel">
      <h2>Leave Applications</h2>

      <table>
        <thead>
          <tr>
            <th>Employee ID</th>
            <th>Leave type</th>
            <th>Days</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="leave in leaves" :key="leave.id">
            <td>{{ leave.empid }}</td>
            <td>{{ leave.leaveType }}</td>
            <td>{{ leave.requestedDays }}</td>
            <td>
              <span class="status" :class="leave.status?.toLowerCase()">
                {{ leave.status }}
              </span>
            </td>
            <td>
              <template v-if="leave.status?.toLowerCase() === 'pending'">
                <button @click="approveLeave(leave.id)">Approve</button>
                <button class="danger" @click="rejectLeave(leave.id)">
                  Reject
                </button>
              </template>

              <span v-else>—</span>
            </td>
          </tr>

          <tr v-if="leaves.length === 0">
            <td colspan="5">No leave applications found.</td>
          </tr>
        </tbody>
      </table>
    </section>

    <!-- Employees by leave type -->
    <section class="panel">
      <h2>Employees by Leave Type</h2>

      <form class="search-form" @submit.prevent="findEmployeesByLeave">
        <label>
          Leave type
          <select v-model="searchLeaveType">
            <option>Vacation</option>
            <option>Casual</option>
            <option>Sick</option>
          </select>
        </label>

        <button type="submit">Find employees</button>
      </form>

      <ul v-if="vacationEmployees.length">
        <li v-for="employee in vacationEmployees" :key="employee.id">
          {{ employee.empid }} — {{ employee.name }} ({{ employee.dept }})
        </li>
      </ul>

      <p v-else class="muted">No matching employees found yet.</p>
    </section>
  </main>
</template>

<style>
* {
  box-sizing: border-box;
}

body {
  margin: 0;
  background: #f3f5fc;
  color: #26324a;
  font-family: Arial, sans-serif;
}

.container {
  max-width: 1100px;
  margin: 32px auto;
  padding: 0 20px;
}

header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
  padding: 22px 26px;
  margin-bottom: 24px;
  background: linear-gradient(135deg, #536dfe, #8b5cf6);
  color: white;
  border-radius: 14px;
}

header h1 {
  margin: 0;
  font-size: 25px;
}

.eyebrow {
  margin: 0 0 8px;
  font-size: 11px;
  letter-spacing: 1.5px;
  opacity: 0.85;
}

.panel {
  background: #ffffff;
  padding: 24px;
  margin: 22px 0;
  border-radius: 14px;
  border: 1px solid #e5e9f5;
  box-shadow: 0 4px 16px #303f6710;
}

.panel h2 {
  margin-top: 0;
  margin-bottom: 20px;
  color: #4655a5;
  font-size: 20px;
}

.admin-panel {
  border-top: 4px solid #7966e8;
}

.admin-panel h2 {
  color: #6b55cc;
}

form {
  display: flex;
  flex-wrap: wrap;
  align-items: end;
  gap: 14px;
}

label {
  display: grid;
  gap: 7px;
  color: #596780;
  font-size: 14px;
  font-weight: 600;
}

input,
select {
  min-width: 150px;
  padding: 11px 12px;
  border: 1px solid #d8def0;
  border-radius: 8px;
  background: #fbfcff;
  color: #26324a;
  font: inherit;
}

input:focus,
select:focus {
  outline: 2px solid #a5b4fc;
  border-color: #6875f5;
}

button {
  padding: 11px 16px;
  border: 0;
  border-radius: 8px;
  background: #5965e8;
  color: white;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s;
}

button:hover {
  background: #414fc9;
}

button.danger {
  background: #e76b7b;
  margin-left: 6px;
}

button.danger:hover {
  background: #cc4f62;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 12px;
}

th,
td {
  padding: 13px 12px;
  text-align: left;
  border-bottom: 1px solid #edf0f7;
}

th {
  background: #eef1ff;
  color: #4b579b;
  font-size: 13px;
}

tbody tr:hover {
  background: #f8f9ff;
}

.status {
  display: inline-block;
  padding: 5px 9px;
  border-radius: 20px;
  background: #eef1ff;
  color: #4b579b;
  font-size: 12px;
  font-weight: 600;
}

.status.approved {
  background: #e8f8ef;
  color: #247548;
}

.status.rejected {
  background: #fff0f0;
  color: #b33a4a;
}

.status.pending {
  background: #fff5df;
  color: #986516;
}

.success {
  padding: 12px 16px;
  background: #e8f8ef;
  color: #247548;
  border-radius: 8px;
}

.error {
  padding: 12px 16px;
  background: #fff0f0;
  color: #b33a4a;
  border-radius: 8px;
}

.search-form {
  margin-bottom: 14px;
}

ul {
  padding-left: 22px;
  color: #4655a5;
}

li {
  padding: 5px 0;
}

.muted {
  color: #7b8498;
}

@media (max-width: 650px) {
  header {
    align-items: flex-start;
    flex-direction: column;
  }

  .panel {
    overflow-x: auto;
  }

  form {
    align-items: stretch;
    flex-direction: column;
  }

  input,
  select {
    width: 100%;
  }
}
</style>
