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
    const deckPopup = document.getElementById('deck-popup');
    const deckList = document.getElementById('deck-list');

    // Open popup on “Add to Deck” knob click
    document.getElementById("channel-knob")?.addEventListener("click", () => {
        if (deckPopup) deckPopup.style.display = "flex";
    });

    // Close popup
    function closeDeckPopup() {
        deckPopup.style.display = "none";
    }

    // Handle deck selection
    deckList?.addEventListener("click", (e) => {
        const selectedDeck = e.target.closest(".popup-deck");
        const wordId = document.querySelector(".deck-btn")?.dataset.wordid;
        if (!selectedDeck || !wordId) return;

        const deckId = selectedDeck.dataset.deckId;

        fetch(`/decks/${deckId}/flashcards/dictionary`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ id: wordId }) // adjust to your WordDto
        })
            .then(res => {
                if (res.ok) {
                    alert("✅ Added to deck!");
                    closeDeckPopup();
                } else {
                    alert("⚠️ Could not add to deck.");
                }
            })
            .catch(err => console.error(err));
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
