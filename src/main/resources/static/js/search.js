    const searchInput = document.getElementById('search-input');
    const remoteSearchBtn = document.getElementById('remote-search');
    const tvStatic = document.querySelector('.tv-static');

    // Audio player
    function playAudio(btn) {
        const url = btn.dataset.audio;
        if (url) {
            const audio = new Audio(url);
            audio.play();
        }
    }

    // Channel change effect
    function triggerStatic(callback) {
        if (!tvStatic) return;
        tvStatic.style.opacity = '1';
        setTimeout(() => {
            tvStatic.style.opacity = '0';
            if (callback) callback();
        }, 400); // 0.4s static effect
    }

    const flashcard = document.getElementById('flashcard');
    const knobs = document.querySelectorAll('.knob');

    knobs.forEach(knob => {
        knob.addEventListener('click', () => {
            triggerStatic(() => {
                flashcard.classList.toggle('flipped');
            });
        });
    });



    function performSearch() {
        const query = searchInput.value.trim();
        if (query) {
            window.location.href = `http://localhost:8080/dictionary/${encodeURIComponent(query)}`;
        }
    }

    remoteSearchBtn?.addEventListener('click', performSearch);

    searchInput?.addEventListener('keydown', (event) => {
        if (event.key === 'Enter') {
            performSearch();
        }
    });
