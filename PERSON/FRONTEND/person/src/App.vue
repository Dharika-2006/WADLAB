<script setup>
import { ref, onMounted } from "vue";
const API = "http://localhost:8080/api/person";

const persons = ref([]);
const name = ref("");
const error = ref("");
const editingId = ref(null);
const loading = ref(false);

//get
async function getPersons() {
  loading.value = true;
  error.value = "";
  try {
    const response = await fetch(API);
    if (!response.ok) {
      throw new Error("Failed to fetch persons");
    }
    persons.value = await response.json();
  } catch (e) {
    error.value = "Cannot connect to backend";
  } finally {
    loading.value = false;
  }
}

//post and put

async function savePerson() {
  if (!name.value.trim()) {
    error.value = "Please enter a name";
    return;
  }
  const isEditing = editingId.value !== null;
  const url = isEditing ? `${API}/${editingId.value}` : API;
  const method = isEditing ? "PUT" : "POST";
  error.value = "";
  try {
    const response = await fetch(url, {
      method: method,
      headers: {
        "Content-type": "application/json",
      },
      body: JSON.stringify({ name: name.value.trim() }),
    });

    if (!response.ok) {
      throw new Error("Failed to save person");
    }
    name.value = "";
    editingId.value = null;
    await getPersons();
  } catch (e) {
    error.value = e.message;
  }
}
//edit person
function editPerson(person) {
  name.value = person.name;
  editingId.value = person.id;
  error.value = "";
}

//cancel edit
function cancelEdit() {
  name.value = "";
  error.value = "";
  editingId.value = null;
}

//DELETE

async function deleteId(id) {
  if (!confirm("You wanna delete")) {
    return;
  }
  try {
    const response = await fetch(`${API}/${id}`, {
      method: "DELETE",
    });
    if (!response.ok) {
      throw new Error("Failed to delete");
    }
    if (editingId.value === id) {
      cancelEdit();
    }
    await getPersons();
  } catch (e) {
    error.value = e.message;
  }
}

onMounted(getPersons);
</script>
<template>
  <div class="container">
    <h1>Person Management System</h1>
    <form @submit.prevent="savePerson">
      <input v-model="name" type="text" placeholder="Enter the name" required />

      <button type="submit">
        {{ editingId !== null ? "Update Person" : "Add Person" }}
      </button>
      <button
        v-if="editingId !== null"
        type="button"
        class="cancel"
        @click="cancelEdit"
      >
        Cancel
      </button>
    </form>
    <p v-if="error" class="error">{{ error }}</p>
    <p v-if="loading">Loading...</p>

    <h2>Person List</h2>
    <p v-if="!loading && persons.length === 0">No persons found.</p>

    <ul v-else>
      <li v-for="person in persons" :key="person.id">
        <div>
          <strong>{{ person.name }}</strong>
          <small>ID : {{ person.id }}</small>
        </div>

        <div class="actions">
          <button class="edit" @click="editPerson(person)">EDIT</button>
          <button class="delete" @click="deleteId(person.id)">Delete</button>
        </div>
      </li>
    </ul>

    <button class="refresh" @click="getPersons">Refresh List</button>
  </div>
</template>
