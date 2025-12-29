// src/main/resources/static/js/import-contacts.js
document.addEventListener('DOMContentLoaded', function() {

    const btnDoImport = document.getElementById('btnDoImport');

    if (btnDoImport) {
        btnDoImport.addEventListener('click', async function() {
            const fileInput = document.getElementById('csvFile');
            const file = fileInput.files[0];
            const messageDiv = document.getElementById('importMessage');

            if (!file) {
                messageDiv.className = "mt-2 small text-danger";
                messageDiv.innerText = "Please select a CSVfile.";
                return;
            }

            if (!file.name.toLowerCase().endsWith('.csv')) {
                messageDiv.className = "mt-2 small text-danger";
                messageDiv.innerText = "Invalid format. Only .csv allowed.";
                return;
            }

            const formData = new FormData();
            formData.append('file', file);

            messageDiv.innerHTML = '<span class="text-primary">Processing...</span>';

            try {
                const response = await fetch('/contacts/import', {
                    method: 'POST',
                    body: formData
                });

                if (response.ok) {
                    messageDiv.innerHTML = '<span class="text-success">Success!. Reloading...</span>';
                    setTimeout(() => window.location.reload(), 1500);
                } else {
                    const errorText = await response.text();
                    messageDiv.innerHTML = `<span class="text-danger">Error: ${errorText}</span>`;
                }
            } catch (error) {
                messageDiv.innerHTML = '<span class="text-danger">Connection error.</span>';
            }
        });
    }
})