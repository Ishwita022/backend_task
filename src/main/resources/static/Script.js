const BASE_URL = "http://localhost:8080/api";

/* =========================
   TOKEN & ROLE HELPERS
========================= */

function getToken() {
    return localStorage.getItem("token");
}

function saveToken(token) {
    localStorage.setItem("token", token);
}

function logout() {
    localStorage.removeItem("token");
    window.location.href = "login.html";
}

function getUserRole() {
    const token = getToken();
    if (!token) return null;

    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        // Normalize role: handle array or string
        if (Array.isArray(payload.role)) return payload.role[0];
        return payload.role;
    } catch (err) {
        console.error("Invalid token:", err);
        return null;
    }
}

/* =========================
   AUTH APIs
========================= */

async function registerUser(data) {
    try {
        const res = await fetch(`${BASE_URL}/register`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        });

        if (!res.ok) {
            const errText = await res.text();
            throw new Error(errText);
        }

        return await res.text();
    } catch (err) {
        console.error("Registration failed:", err);
        return null;
    }
}

async function registerAdmin(data) {
    try {
        const token = getToken(); // Must be logged in as admin
        if (!token) throw new Error("Admin login required");

        const res = await fetch(`${BASE_URL}/admin/register`, {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

        if (!res.ok) {
            const errText = await res.text();
            throw new Error(errText);
        }

        return await res.text();
    } catch (err) {
        console.error("Admin registration failed:", err);
        return null;
    }
}

async function loginUser(data) {
    try {
        const res = await fetch(`${BASE_URL}/login`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        });

        if (res.ok) {
            return await res.text(); // JWT token
        } else {
            const errText = await res.text();
            throw new Error(errText);
        }
    } catch (err) {
        console.error("Login failed:", err);
        return null;
    }
}

/* =========================
   TASK APIs
========================= */

async function getTasks() {
    try {
        const res = await fetch(`${BASE_URL}/tasks`, {
            headers: { "Authorization": "Bearer " + getToken() }
        });
        if (!res.ok) throw new Error(await res.text());
        return await res.json();
    } catch (err) {
        console.error("Failed to fetch tasks:", err);
        return [];
    }
}

async function createTask(task) {
    try {
        const res = await fetch(`${BASE_URL}/tasks`, {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + getToken(),
                "Content-Type": "application/json"
            },
            body: JSON.stringify(task)
        });
        if (!res.ok) throw new Error(await res.text());
        await loadTasks();
    } catch (err) {
        console.error("Failed to create task:", err);
    }
}

async function deleteTask(id) {
	try {
	const token = localStorage.getItem("token");

	   const response = await fetch(`http://localhost:8080/api/tasks/${id}`, {
	       method: "DELETE",
	       headers: {
	           "Authorization": "Bearer " + token
	       }
	   });

	   if (response.ok) {
	       alert("Deleted successfully");
	       loadTasks();  // refresh full list
	   } else {
	       alert("Delete failed");
	   	   }
		   }	   catch (err) {
	           console.error("Failed to toggle task:", err);
	       }
}

async function toggleTask(id, currentStatus) {
    try {
        const res = await fetch(`${BASE_URL}/tasks/${id}`, {
            method: "PUT",
            headers: {
                "Authorization": "Bearer " + getToken(),
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ completed: !currentStatus })
        });
        if (!res.ok) throw new Error(await res.text());
        await loadTasks();
    } catch (err) {
        console.error("Failed to toggle task:", err);
    }
}


function editTask(id, title, description) {
    const newTitle = prompt("Edit Title:", title);
    if (newTitle === null) return; // cancelled

    const newDesc = prompt("Edit Description:", description);
    if (newDesc === null) return; // cancelled

    updateTask(id, { title: newTitle, description: newDesc });
}

async function updateTask(id, updatedData) {
    try {
        const res = await fetch(`${BASE_URL}/tasks/${id}`, {
            method: "PUT",
            headers: {
                "Authorization": "Bearer " + getToken(),
                "Content-Type": "application/json"
            },
            body: JSON.stringify(updatedData)
        });

        if (!res.ok) throw new Error(await res.text());
        alert("Task updated successfully!");
        loadTasks(); // refresh list
    } catch (err) {
        console.error("Failed to update task:", err);
        alert("Update failed!");
    }
}

/* =========================
   LOAD TASKS (ROLE BASED UI)
========================= */

async function loadTasks() {
    const token = getToken();
    if (!token) {
        alert("Login first");
        window.location.href = "login.html";
        return;
    }

    const role = getUserRole();
    const tasks = await getTasks();

    const list = document.getElementById("taskList");
    if (!list) return;

    list.innerHTML = "";

    if (tasks.length === 0) {
        list.innerHTML = "<p>No tasks found.</p>";
        return;
    }

    tasks.forEach(t => {
        const div = document.createElement("div");
        div.className = "task-item";
        div.style.border = "1px solid #ccc";
        div.style.marginBottom = "10px";
        div.style.padding = "10px";
        div.style.borderRadius = "8px";

        div.innerHTML = `
            <div class="task-title"><strong>${t.title || "Untitled"}</strong></div>
            <div class="task-meta">
                ${t.description || "No description"}
                ${role === "ROLE_ADMIN" ? `<br><small>Created By: ${t.createdBy}</small>` : ""}
                <br>Status: ${t.completed ? "✅ Completed" : "⏳ Pending"}
            </div>
            <br>
			<button style="background:blue;color:white;padding:6px 12px;margin-right:8px;border:none;border-radius:4px;cursor:pointer;"
			       onclick="editTask(${t.id}, '${t.title}', '${t.description}')">
			       ✏️ Edit
			   </button>
			   <button style="background:green;color:white;padding:6px 12px;margin-right:8px;border:none;border-radius:4px;cursor:pointer;"
			       onclick="toggleTask(${t.id}, ${t.completed})">
			       Toggle Complete
			   </button>
			   <button style="background:red;color:white;padding:6px 12px;border:none;border-radius:4px;cursor:pointer;"
			       onclick="deleteTask(${t.id})">
			       🗑️ Delete
			   </button>
        `;

        list.appendChild(div);
    });

    // Update heading
    const heading = document.querySelector("h2");
    if (heading) {
        heading.innerText = role === "ROLE_ADMIN" ? "Admin - All Users Tasks" : "My Tasks";
    }
}

/* =========================
   FRONTEND ADMIN HELPER
========================= */

// Call this to register a new admin (must be logged in as admin)
async function createAdmin(name, email, password) {
    const result = await registerAdmin({ name, email, password });
    if (result) alert("Admin registered successfully!");
    else alert("Failed to register admin");
}
