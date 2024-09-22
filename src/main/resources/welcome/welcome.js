document.getElementById('searchButton').addEventListener('click', function () {
    const query = document.getElementById('searchBar').value.trim();
    if (query) {
        if (query.includes('.')) window.location.href = query.startsWith('http://') || query.startsWith('https://') ? query : 'https://' + query;
        else window.location.href = 'https://www.google.com/search?q=' + encodeURIComponent(query);
    }
});

document.getElementById('searchBar').addEventListener('keypress', function (event) {
    if (event.key === 'Enter') document.getElementById('searchButton').click();
});
