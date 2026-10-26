<script setup>
import { ref, onMounted } from "vue";

const API = "http://localhost:8080/api";

const courses = ref([]);
const enrollments = ref([]);

const teacher = ref("");
const studentName = ref("");
const courseid = ref("");

const message = ref("");
const error = ref("");

const newcourse = ref({
  courseid: "",
  coursename: "",
  teacher: "",
  credits: 3,
  seats: 30,
});

const enrollForm = ref({
  enrollid: "",
  courseid: "",
  studentName: "",
});

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
    } catch (e) {
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

// ---------- courses collection ----------
async function getCourses() {
  courses.value = await request("/courses");
}

async function getByTeacher() {
  error.value = "";
  try {
    const t = encodeURIComponent(teacher.value);
    courses.value = await request(`/courses/teacher/${t}`);
  } catch (e) {
    error.value = e.message;
  }
}

// ---------- queries using both collections ----------
async function getSelected() {
  error.value = "";
  try {
    courses.value = await request("/courses/selected");
  } catch (e) {
    error.value = e.message;
  }
}

async function getByStudent() {
  error.value = "";
  try {
    const s = encodeURIComponent(studentName.value);
    courses.value = await request(`/courses/student/${s}`);
  } catch (e) {
    error.value = e.message;
  }
}

async function getStudentsByCourse() {
  error.value = "";
  try {
    const c = encodeURIComponent(courseid.value);
    enrollments.value = await request(`/enrollments/course/${c}`);
  } catch (e) {
    error.value = e.message;
  }
}

async function getStudentsByTeacher() {
  error.value = "";
  try {
    const t = encodeURIComponent(teacher.value);
    enrollments.value = await request(`/teachers/${t}/students`);
  } catch (e) {
    error.value = e.message;
  }
}

async function refreshAll() {
  error.value = "";
  try {
    await getCourses();
  } catch (e) {
    error.value = e.message;
  }
}

// ---------- add course ----------
async function addCourse() {
  error.value = "";
  message.value = "";
  try {
    await request("/courses", {
      method: "POST",
      body: JSON.stringify({
        courseid: newcourse.value.courseid,
        coursename: newcourse.value.coursename,
        teacher: newcourse.value.teacher,
        credits: Number(newcourse.value.credits),
        seats: Number(newcourse.value.seats),
      }),
    });
    message.value = "Course added successfully!";
    newcourse.value = {
      courseid: "",
      coursename: "",
      teacher: "",
      credits: 3,
      seats: 30,
    };
    await getCourses();
  } catch (e) {
    error.value = e.message;
  }
}

// ---------- enroll / drop ----------
async function enroll() {
  error.value = "";
  message.value = "";
  try {
    const created = await request("/enrollments", {
      method: "POST",
      body: JSON.stringify({
        enrollid: enrollForm.value.enrollid,
        courseid: enrollForm.value.courseid,
        studentName: enrollForm.value.studentName,
        status: "",
      }),
    });
    message.value = `Enrolled! Status: ${created.status}`;
    enrollForm.value = { enrollid: "", courseid: "", studentName: "" };
    await getCourses(); // seats count drops
  } catch (e) {
    error.value = e.message; // e.g. "No seats available"
  }
}

async function dropEnrollment(enrollid) {
  error.value = "";
  message.value = "";
  try {
    const updated = await request(`/enrollments/${enrollid}/drop`, {
      method: "PUT",
    });
    message.value = `Enrollment ${updated.status.toLowerCase()}.`;
    enrollments.value = enrollments.value.map((x) =>
      x.enrollid === updated.enrollid ? updated : x,
    );
    await getCourses(); // seat given back
  } catch (e) {
    error.value = e.message;
  }
}

onMounted(refreshAll);
</script>

<template>
  <main class="container">
    <header>
      <h1>Course Management</h1>
      <button @click="refreshAll">Show all courses</button>
    </header>

    <p v-if="message" class="success">{{ message }}</p>
    <p v-if="error" class="error">{{ error }}</p>

    <!-- Add course -->
    <section class="panel">
      <h2>Add Course</h2>
      <form @submit.prevent="addCourse">
        <label>Course ID
          <input v-model.trim="newcourse.courseid" placeholder="e.g. C101" required />
        </label>
        <label>Course name
          <input v-model.trim="newcourse.coursename" placeholder="Enter name" required />
        </label>
        <label>Teacher
          <input v-model.trim="newcourse.teacher" placeholder="e.g. Dr. Rao" required />
        </label>
        <label>Credits
          <input v-model.number="newcourse.credits" type="number" min="1" required />
        </label>
        <label>Seats
          <input v-model.number="newcourse.seats" type="number" min="0" required />
        </label>
        <button type="submit">+ Add Course</button>
      </form>
    </section>

    <!-- Enroll -->
    <section class="panel">
      <h2>Student Selects a Course</h2>
      <form @submit.prevent="enroll">
        <label>Enrollment ID
          <input v-model.trim="enrollForm.enrollid" placeholder="e.g. E101" required />
        </label>
        <label>Course
          <select v-model="enrollForm.courseid" required>
            <option value="" disabled>Select course</option>
            <option v-for="c in courses" :key="c.id" :value="c.courseid">
              {{ c.courseid }} - {{ c.coursename }}
            </option>
          </select>
        </label>
        <label>Student name
          <input v-model.trim="enrollForm.studentName" placeholder="Enter name" required />
        </label>
        <button type="submit">Enroll</button>
      </form>
    </section>

    <!-- Queries -->
    <section class="panel">
      <h2>Queries</h2>

      <form class="filters" @submit.prevent="getByTeacher">
        <label>Teacher
          <input v-model.trim="teacher" placeholder="e.g. Dr. Rao" required />
        </label>
        <button type="submit">Courses taken by teacher</button>
        <button type="button" @click="getStudentsByTeacher">Students of this teacher</button>
      </form>

      <form class="filters" @submit.prevent="getByStudent">
        <label>Student
          <input v-model.trim="studentName" placeholder="Enter name" required />
        </label>
        <button type="submit">Courses selected by student</button>
      </form>

      <form class="filters" @submit.prevent="getStudentsByCourse">
        <label>Course ID
          <input v-model.trim="courseid" placeholder="e.g. C101" required />
        </label>
        <button type="submit">Students of this course</button>
      </form>

      <button @click="getSelected">Courses selected by students</button>
    </section>

    <!-- Courses table -->
    <section class="panel">
      <h2>Courses</h2>
      <table>
        <thead>
          <tr>
            <th>Course ID</th>
            <th>Name</th>
            <th>Teacher</th>
            <th>Credits</th>
            <th>Seats left</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="c in courses" :key="c.id">
            <td>{{ c.courseid }}</td>
            <td>{{ c.coursename }}</td>
            <td>{{ c.teacher }}</td>
            <td>{{ c.credits }}</td>
            <td>{{ c.seats }}</td>
          </tr>
          <tr v-if="courses.length === 0">
            <td colspan="5">No courses found.</td>
          </tr>
        </tbody>
      </table>
    </section>

    <!-- Enrollments table -->
    <section class="panel">
      <h2>Enrollments</h2>
      <table>
        <thead>
          <tr>
            <th>Enrollment ID</th>
            <th>Course ID</th>
            <th>Student</th>
            <th>Status</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="e in enrollments" :key="e.id">
            <td>{{ e.enrollid }}</td>
            <td>{{ e.courseid }}</td>
            <td>{{ e.studentName }}</td>
            <td>
              <span class="status" :class="e.status?.toLowerCase()">{{ e.status }}</span>
            </td>
            <td>
              <button
                v-if="e.status?.toLowerCase() === 'enrolled'"
                class="danger"
                @click="dropEnrollment(e.enrollid)"
              >
                Drop
              </button>
              <span v-else>-</span>
            </td>
          </tr>
          <tr v-if="enrollments.length === 0">
            <td colspan="5">Use a query above to see students.</td>
          </tr>
        </tbody>
      </table>
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

.panel {
  background: #ffffff;
  padding: 24px;
  margin: 22px 0;
  border-radius: 14px;
  border: 1px solid #e5e9f5;
}

.panel h2 {
  margin-top: 0;
  margin-bottom: 20px;
  color: #4655a5;
  font-size: 20px;
}

form {
  display: flex;
  flex-wrap: wrap;
  align-items: end;
  gap: 14px;
}

.filters {
  margin-bottom: 8px;
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

input:disabled {
  background: #eef0f6;
  color: #7b8498;
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

button.secondary {
  background: #8a93b2;
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