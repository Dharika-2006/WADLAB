<script setup>
import { ref, onMounted } from "vue";

const API = "http://localhost:8080/api";

const hotels = ref([]);

const location = ref("");
const roomtype = ref("Single");

const message = ref("");
const error = ref("");

const newhotel = ref({
  hotelid: "",
  hotelname: "",
  location: "",
  roomType: "Single",
  price: 0.0,
  availableRooms: 0,
});

const booking = ref({
  bookingid: "",
  hotelid: "",
  customerName: "",
  guest: 1,
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

async function getHotels() {
  hotels.value = await request("/hotels");
}

async function getByLocation() {
  error.value = "";
  try {
    const loc = encodeURIComponent(location.value);
    hotels.value = await request(`/hotels/location/${loc}`);
  } catch (e) {
    error.value = e.message;
  }
}

async function getAvailable() {
  error.value = "";
  try {
    const type = encodeURIComponent(roomtype.value);
    hotels.value = await request(`/hotels/available/${type}`);
  } catch (e) {
    error.value = e.message;
  }
}

async function getByGuest() {
  error.value = "";
  try {
    hotels.value = await request("/hotels/guests");
  } catch (e) {
    error.value = e.message;
  }
}

async function refreshAll() {
  error.value = "";
  try {
    await getHotels();
  } catch (e) {
    error.value = e.message;
  }
}

async function addHotel() {
  error.value = "";
  message.value = "";
  try {
    await request("/hotels", {
      method: "POST",
      body: JSON.stringify({
        hotelid: newhotel.value.hotelid,
        hotelname: newhotel.value.hotelname,
        location: newhotel.value.location,
        roomType: newhotel.value.roomType,
        price: Number(newhotel.value.price),
        availableRooms: Number(newhotel.value.availableRooms),
      }),
    });
    message.value = "Hotel added successfully!";
    newhotel.value = {
      hotelid: "",
      hotelname: "",
      location: "",
      roomType: "Single",
      price: 0.0,
      availableRooms: 0,
    };
    await getHotels();
  } catch (e) {
    error.value = e.message;
  }
}

async function bookRoom() {
  error.value = "";
  message.value = "";
  try {
    const created = await request("/bookings", {
      method: "POST",
      body: JSON.stringify({
        bookingid: booking.value.bookingid,
        hotelid: booking.value.hotelid,
        customerName: booking.value.customerName,
        guest: Number(booking.value.guest),
        status: "",
      }),
    });
    message.value = `Room booked! Status: ${created.status}`;
    booking.value = { bookingid: "", hotelid: "", customerName: "", guest: 1 };
    await getHotels(); // available rooms count drops
  } catch (e) {
    error.value = e.message; // e.g. "No rooms available"
  }
}

onMounted(refreshAll);
</script>

<template>
  <main class="container">
    <header>
      <h1>Hotel Booking</h1>
      <button @click="refreshAll">Show all hotels</button>
    </header>

    <p v-if="message" class="success">{{ message }}</p>
    <p v-if="error" class="error">{{ error }}</p>

    <!-- Add hotel -->
    <section class="panel">
      <h2>Add Hotel</h2>
      <form @submit.prevent="addHotel">
        <label
          >Hotel ID
          <input
            v-model.trim="newhotel.hotelid"
            placeholder="e.g. H101"
            required
          />
        </label>
        <label
          >Hotel name
          <input
            v-model.trim="newhotel.hotelname"
            placeholder="Enter name"
            required
          />
        </label>
        <label
          >Location
          <input
            v-model.trim="newhotel.location"
            placeholder="e.g. Chennai"
            required
          />
        </label>
        <label
          >Room type
          <select v-model="newhotel.roomType">
            <option>Single</option>
            <option>Double</option>
            <option>Deluxe</option>
          </select>
        </label>
        <label
          >Price
          <input
            v-model.number="newhotel.price"
            type="number"
            min="0"
            required
          />
        </label>
        <label
          >Available rooms
          <input
            v-model.number="newhotel.availableRooms"
            type="number"
            min="0"
            required
          />
        </label>
        <button type="submit">+ Add Hotel</button>
      </form>
    </section>

    <!-- Book room -->
    <section class="panel">
      <h2>Book a Room</h2>
      <form @submit.prevent="bookRoom">
        <label
          >Booking ID
          <input
            v-model.trim="booking.bookingid"
            placeholder="e.g. B101"
            required
          />
        </label>
        <label
          >Hotel
          <select v-model="booking.hotelid" required>
            <option value="" disabled>Select hotel</option>
            <option v-for="h in hotels" :key="h.id" :value="h.hotelid">
              {{ h.hotelid }} - {{ h.hotelname }} ({{ h.roomType }})
            </option>
          </select>
        </label>
        <label
          >Customer name
          <input
            v-model.trim="booking.customerName"
            placeholder="Enter name"
            required
          />
        </label>
        <label
          >Guests
          <input
            v-model.number="booking.guest"
            type="number"
            min="1"
            required
          />
        </label>
        <button type="submit">Book</button>
      </form>
    </section>

    <!-- Search / filters -->
    <section class="panel">
      <h2>Search Hotels</h2>
      <form class="filters" @submit.prevent="getByLocation">
        <label
          >Location
          <input v-model.trim="location" placeholder="e.g. Chennai" required />
        </label>
        <button type="submit">Find by location</button>
      </form>

      <form class="filters" @submit.prevent="getAvailable">
        <label
          >Room type
          <select v-model="roomtype">
            <option>Single</option>
            <option>Double</option>
            <option>Deluxe</option>
          </select>
        </label>
        <button type="submit">Show available rooms</button>
      </form>

      <button @click="getByGuest">
        Hotels with confirmed bookings of more than 3 guests
      </button>
    </section>

    <!-- Hotels table -->
    <section class="panel">
      <h2>Hotels</h2>
      <table>
        <thead>
          <tr>
            <th>Hotel ID</th>
            <th>Name</th>
            <th>Location</th>
            <th>Room type</th>
            <th>Price</th>
            <th>Available rooms</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="h in hotels" :key="h.id">
            <td>{{ h.hotelid }}</td>
            <td>{{ h.hotelname }}</td>
            <td>{{ h.location }}</td>
            <td>{{ h.roomType }}</td>
            <td>{{ h.price }}</td>
            <td>{{ h.availableRooms }}</td>
          </tr>
          <tr v-if="hotels.length === 0">
            <td colspan="6">No hotels found.</td>
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
