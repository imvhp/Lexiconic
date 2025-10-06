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

    // Flip card (volume knob)
    document.getElementById("volume-knob")?.addEventListener("click", () => {
        triggerStatic(() => {
            flashcard.classList.toggle("flipped");
        })
    });

    // Add to Deck (channel knob)
    document.getElementById("channel-knob")?.addEventListener("click", () => {
        const wordId = document.querySelector(".deck-btn")?.dataset.wordid;

        if (wordId) {
            fetch(`/deck/add/${wordId}`, { method: "POST" })
                .then(res => {
                    if (res.ok) {
                        alert("✅ Word added to deck!");
                        // channel-change effect
                        const tv = document.querySelector(".tv-static");
                        tv.classList.add("channel-change");
                        setTimeout(() => tv.classList.remove("channel-change"), 600);
                    } else {
                        alert("⚠️ Could not add to deck.");
                    }
                });
        }
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
