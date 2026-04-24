const API_URL = 'http://localhost:8081/test';

document.getElementById('send-single').addEventListener('click', () => {
    sendRequest(1);
});

document.getElementById('send-burst').addEventListener('click', () => {
    // Clear previous results before a burst test
    document.getElementById('results-container').innerHTML = '';
    for (let i = 1; i <= 6; i++) {
        sendRequest(i);
    }
});

async function sendRequest(reqNumber = 1) {
    const resultsContainer = document.getElementById('results-container');
    const resultItem = document.createElement('div');
    resultItem.className = 'result-item';
    
    try {
        const response = await fetch(API_URL);
        
        if (response.ok) {
            const data = await response.text(); 
            resultItem.classList.add('success');
            resultItem.innerHTML = `
                <span>Request #${reqNumber}: ${data || 'Success'}</span>
                <span class="status-badge">${response.status} OK</span>
            `;
        } else if (response.status === 429) {
            resultItem.classList.add('error');
            resultItem.innerHTML = `
                <span>Request #${reqNumber}: Rate limit exceeded</span>
                <span class="status-badge">429</span>
            `;
        } else {
            resultItem.classList.add('error');
            resultItem.innerHTML = `
                <span>Request #${reqNumber}: Error</span>
                <span class="status-badge">${response.status}</span>
            `;
        }
    } catch (error) {
        resultItem.classList.add('error');
        resultItem.innerHTML = `
            <span>Request #${reqNumber}: Network Error or CORS issue</span>
            <span class="status-badge">Failed</span>
        `;
    }
    
    // Append to keep the order from top to bottom
    resultsContainer.appendChild(resultItem);
}
