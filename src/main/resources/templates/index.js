async function init() {

    const registration = await navigator.serviceWorker.register('/sw.js');
    await navigator.serviceWorker.ready;
    firebase.initializeApp({
        messagingSenderId: "376524768909"
    });
    const messaging = firebase.messaging();
    messaging.usePublicVapidKey('BCch2TlbaCrk89wx-Ux_36H0etaAVBF_ofkgYrY4jh9sk3xtoSkSvAKhJNQvIh4RXf1UcrSi8VNlt2eLYttJPXA');
    messaging.useServiceWorker(registration);

    try {
        await messaging.requestPermission();
    } catch (e) {
        console.log('Unable to get permission', e);
        return;
    }

    navigator.serviceWorker.addEventListener('message', event => {
        if (event.data === 'newData') {
            showData();
        }
    });

    const currentToken = await messaging.getToken();
    fetch('/register', { method: 'post', body: currentToken });
    showData();

    messaging.onTokenRefresh(async () => {
        console.log('token refreshed');
        const newToken = await messaging.getToken();
        fetch('/register', { method: 'post', body: currentToken });
    });

}

async function showData() {
    const db = await getDb();
    const tx = db.transaction('tasks', 'readonly');
    const store = tx.objectStore('tasks');
    store.getAll().onsuccess = e => showJokes(e.target.result);
}

function showJokes(tasks) {
    const table = document.getElementById('outTable');

    tasks.sort((a, b) => parseInt(b.ts) - parseInt(a.ts));
    const html = [];
    tasks.forEach(j => {
        const date = new Date(parseInt(j.ts));
        html.push(`<div><div class="header">${date.toISOString()} ${j.id} (${j.seq})</div><div class="task">${j.task}</div></div>`);
    });
    table.innerHTML = html.join('');
}

async function getDb() {
    if (this.db) {
        return Promise.resolve(this.db);
    }
    return new Promise(resolve => {
        const openRequest = indexedDB.open("Chuck", 1);

        openRequest.onupgradeneeded = event => {
            const db = event.target.result;
            db.createObjectStore('tasks', { keyPath: 'id' });
        };

        openRequest.onsuccess = event => {
            this.db = event.target.result;
            resolve(this.db);
        }
    });
}

init();