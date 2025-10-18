const sidebar = document.querySelector('.sidebar');
const toggleBtn = document.getElementById('toggle-btn');
const content = document.querySelector('.content');

function toggleSidebar() {
    if (window.innerWidth <= 480) {
        // On small screens, toggle overlay
        sidebar.classList.toggle('show-sidebar');
    } else {
        // On desktop, toggle minimize
        sidebar.classList.toggle('minimize');
        content.classList.toggle('expand', sidebar.classList.contains('minimize'));
    }
}

toggleBtn.addEventListener('click', toggleSidebar);

// Handle resizing between breakpoints
function handleResize() {
    if (window.innerWidth <= 480) {
        // Mobile: reset desktop states
        sidebar.classList.remove('minimize');
        content.classList.remove('expand');
        sidebar.classList.remove('show-sidebar'); // start hidden
    } else {
        // Desktop: reset mobile overlay ONLY
        sidebar.classList.remove('show-sidebar');
        // keep minimize/expand as user last set them
    }
}

// In your existing sidebar toggle code, add this:
toggleBtn.addEventListener('click', () => {
    const sidebar = document.querySelector('.sidebar');
    const body = document.body;

    if (sidebar.classList.contains('minimize')) {
        body.classList.add('sidebar-minimized');
    } else {
        body.classList.remove('sidebar-minimized');
    }
});

window.addEventListener('resize', handleResize);
handleResize();


// Set active link based on current URL
document.addEventListener('DOMContentLoaded', () => {
    const links = document.querySelectorAll('.sidebar .nav-link');
    const currentPath = window.location.pathname;

    links.forEach(link => {
        // Remove active class from all
        link.classList.remove('active');

        // Add active class if href matches current path
        const href = link.getAttribute('href');

        if (href === currentPath || currentPath.startsWith(href)) {
            link.classList.add('active');
        }
    });

    // Handle click to update active state
    links.forEach(link => {
        link.addEventListener('click', () => {
            links.forEach(l => l.classList.remove('active'));
            link.classList.add('active');
        });
    });
});
