const API_SERVER = "http://localhost:8080/iot/hub/api"

document.addEventListener('DOMContentLoaded', () => {
    document.getElementById("add-form").addEventListener("submit", (event) => {
        event.preventDefault();

        addDevice().then(() => {});
    });

    getDevices().then(() => {});
});


function populateTable(data) {
    console.log("[DATA] -> ", data);

    const json = JSON.parse(data);
    const devices = new Map();

    Object.entries(json).forEach(([key, value]) => {
        const map = new Map();

        Object.entries(value).forEach(([innerKey, innerValue]) => {
           map.set(innerKey, innerValue);
        });

        devices.set(key, map);
    });

    const container = document.querySelector("#devices-table-container");

    if (devices.size === 0) {
        container.innerHTML = `<p>No devices found</p>`;
    } else {

        container.innerHTML = `
            <table>
                <caption><p>Registered devices</p></caption>
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Address</th>
                        <th>Port</th>
                        <th class="devices-delete-column"></th>
                    </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        `;

        const tbody = document.querySelector("#devices-table-container table tbody")

        for (let key of devices.keys()) {
            const device = devices.get(key);

            console.log(key, ": ", device);

            // <tr>

            const row = document.createElement('tr');

            // <td> -> Name

            const nameCell = document.createElement('td');

            const namePar = document.createElement('p');
            namePar.classList.add("pointer");
            namePar.addEventListener("click", () => {
                openDetail(`${key}`);
            })
            namePar.textContent = `${device.get("name")}`;

            nameCell.appendChild(namePar);

            // </td> -> Name
            // <td> -> Address

            const addressCell = document.createElement('td');
            addressCell.textContent = `${device.get("address")}`;

            // </td> -> Address
            // <td> -> Port

            const portCell = document.createElement('td');
            portCell.textContent = `${device.get("port")}`;

            // </td> -> Port
            // <td> -> Delete

            const deleteCell = document.createElement('td');

            const deleteIcon = document.createElement('i');
            deleteIcon.classList.add("fa-solid", "fa-trash", "pointer");
            deleteIcon.addEventListener("click", () => {
                removeDevice(`${key}`);
            });

            deleteCell.appendChild(deleteIcon);

            // </td> -> Delete

            row.appendChild(nameCell);
            row.appendChild(addressCell);
            row.appendChild(portCell);
            row.appendChild(deleteCell);

            // </tr>

            tbody.appendChild(row);
        }
    }
}

async function populateDetailDialog(data) {
    console.log("[DATA] -> ", data);

    const json = JSON.parse(data);

    let dialog = document.getElementById("detail-dialog");
    let header = document.getElementById("detail-header");
    let container = document.getElementById("detail-data");

    header.innerHTML = `
        <h2>${json.name}</h2>
        <h5>${json.address}:${json.port}</h5>
        <i
                class="icon-dialog-close fa-solid fa-xmark"
                onclick="document.getElementById('detail-dialog').close();"
        >
        </i>
    `;

    container.innerHTML = "";

    let resources = json.resources;

    if (resources.length > 0) {
        for (const resource of resources) {
            const response = await fetch(`${API_SERVER}/detail/${json.name}/${resource}`);

            let resourceData = "{}";

            if (response.ok) {
                resourceData = await response.text();

                console.log(`[RESOURCE] ${resource} -> ${resourceData}`);

                let resourceJson = JSON.parse(resourceData);

                try {
                    for (const responseArray of resourceJson) {
                        let htmlStr =
                            `
                            <table>
                                <thead>
                                    <tr>
                                        <th>Field</th>
                                        <th>Value</th>
                                    </tr>
                                </thead>
                                <tbody>
                            `;

                        Object.entries(responseArray).forEach(([key, value]) => {
                            htmlStr +=
                                `
                                <tr>
                                    <td>${key}</td>
                                    <td>${value}</td>
                                </tr>
                                `;
                        });

                        htmlStr += `
                        </tbody></table>`;

                        container.innerHTML += htmlStr;
                    }
                }
                catch (TypeError) {
                    let htmlStr =
                        `
                            <table>
                                <thead>
                                    <tr>
                                        <th>Field</th>
                                        <th>Value</th>
                                    </tr>
                                </thead>
                                <tbody>
                            `;

                    Object.entries(resourceJson).forEach(([key, value]) => {
                        htmlStr +=
                            `
                                <tr>
                                    <td>${key}</td>
                                    <td>${value}</td>
                                </tr>
                                `;
                    });

                    htmlStr += `
                        </tbody></table>`;

                    container.innerHTML += htmlStr;
                }
            }
        }
    }
    else {
        container.innerHTML =
            `
            <h3>Error: no resources found</h3>
            <p>It was impossible to get the device data.</p>
            <p>Check the connection or the device status</p>
            `;
    }

    dialog.show();
}

async function openDetail(key) {
    try {
        const response = await fetch(`${API_SERVER}/detail/${key}`);

        let data = "{}";

        if (response.ok) {
            data = await response.text();
        }

        populateDetailDialog(data);
    } catch (error) {
        console.error("Error: ", error);
    }
}

async function getDevices() {
    try {
        const response = await fetch(`${API_SERVER}/list/`);

        let data = "{}";

        if (response.ok) {
            data = await response.text();
        }

        populateTable(data);
    } catch (error) {
        console.error("Error:", error);
    }
}

async function addDevice() {
    const form = document.getElementById("add-form");

    const name = form.querySelector(`input[name="dname"]`).value;
    const address = form.querySelector(`input[name="address"]`).value;
    const port = parseInt(form.querySelector(`input[name="port"]`).value);

    console.log(`Name: ${name}`);
    console.log(`Address: ${address}`);
    console.log(`Port: ${port}`);

    const data = {
        name: name,
        address: address,
        port: port
    };

    try {
        const json = JSON.stringify(data);

        console.log(`Data: ${data}\nJSON: ${json}`);

        const response = await fetch(`${API_SERVER}/list/`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: json
        });

        console.log(response);

        if (response.ok) {
            window.alert("Device added successfully");

            document.getElementById("add-dialog").close();

            await getDevices();
        } else {
            window.alert(`Device not added: Code ${response.status}`);
        }
    } catch(error) {
        console.log("Error adding device: ", error)
        window.alert(`Error adding the device: \n${error}`);
    }
}

function removeDevice(key) {
    if (confirm(`Are you sure you want to remove the "${key}" device?`)) {
        try {
            const url = `${API_SERVER}/list/${key}/`;
            console.log("Sending request to ", url);

            fetch(url, {
                method: 'DELETE'
            }).then(response => {
                console.log(response);

                if (response.ok) {
                    window.alert("Device removed successfully");
                    getDevices().then(() => {});
                }
                else {
                    window.alert(`Device not removed: Code ${response.status}`);
                }
            });


        } catch (error) {
            console.error('Error: ', error);
            window.alert(`Error removing the device: \n${error}`);
        }
    }
}