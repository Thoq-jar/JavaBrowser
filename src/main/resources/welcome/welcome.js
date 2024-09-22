const sysLinks = [
    'about',
    'game',
    'error',
    'welcome'
];

$(document).ready(function () {
    $('#searchButton').on('click', function () {
        const query = $('#searchBar').val().trim();
        if (query) {
            if (query.startsWith('purr://')) {
                parseSysLink(query);
            } else if (query.includes('.')) {
                window.location.href = query.startsWith('http://') || query.startsWith('https://') ? query : 'https://' + query;
            } else {
                window.location.href = 'https://www.google.com/search?q=' + encodeURIComponent(query);
            }
        }
    });

    $('#searchBar').on('keydown', function (event) {
        if (event.key === 'Enter') $('#searchButton').click();
    });

    function parseSysLink(link) {
        const sysLink = link.replace('purr://', '');
        if (sysLinks.includes(sysLink)) window.location.href = `${sysLink}.html`;
    }
});