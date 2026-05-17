// ── Mobile menu toggle ─────────────────────────────────
const hamburger = document.getElementById('hamburger');
const mobileMenu = document.getElementById('mobileMenu');

if (hamburger && mobileMenu) {
    hamburger.addEventListener('click', () => {
        mobileMenu.classList.toggle('open');
        hamburger.setAttribute('aria-expanded', mobileMenu.classList.contains('open'));
    });
}

// ── Scroll reveal motion ───────────────────────────────
const revealItems = document.querySelectorAll('.reveal-on-scroll');

if ('IntersectionObserver' in window && revealItems.length > 0) {
    const revealObserver = new IntersectionObserver(entries => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('is-visible');
                revealObserver.unobserve(entry.target);
            }
        });
    }, { threshold: 0.16, rootMargin: '0px 0px -40px 0px' });

    revealItems.forEach(item => revealObserver.observe(item));
} else {
    revealItems.forEach(item => item.classList.add('is-visible'));
}

// ── Product card tilt interaction ──────────────────────
document.querySelectorAll('.tilt-card').forEach(card => {
    card.addEventListener('pointermove', event => {
        const rect = card.getBoundingClientRect();
        const x = ((event.clientX - rect.left) / rect.width - 0.5) * 2;
        const y = ((event.clientY - rect.top) / rect.height - 0.5) * -2;

        card.style.setProperty('--tilt-x', `${x * 3.5}deg`);
        card.style.setProperty('--tilt-y', `${y * 3.5}deg`);
    });

    card.addEventListener('pointerleave', () => {
        card.style.removeProperty('--tilt-x');
        card.style.removeProperty('--tilt-y');
    });
});

// ── Quantity controls ──────────────────────────────────
document.querySelectorAll('.qty-control').forEach(control => {
    const input = control.querySelector('input[type="number"]');
    const dec   = control.querySelector('[data-action="dec"]');
    const inc   = control.querySelector('[data-action="inc"]');

    if (dec && inc && input) {
        dec.addEventListener('click', () => {
            const val = parseInt(input.value) || 1;
            if (val > 1) input.value = val - 1;
        });
        inc.addEventListener('click', () => {
            const val = parseInt(input.value) || 1;
            input.value = val + 1;
        });
    }
});

// ── Add-to-cart feedback ───────────────────────────────
document.querySelectorAll('.add-to-cart-btn').forEach(button => {
    button.addEventListener('click', () => {
        button.classList.remove('is-added');
        requestAnimationFrame(() => button.classList.add('is-added'));
    });
});

// ── Auto-dismiss flash messages ────────────────────────
document.querySelectorAll('.flash').forEach(flash => {
    setTimeout(() => {
        flash.style.transition = 'opacity 0.5s';
        flash.style.opacity = '0';
        setTimeout(() => flash.remove(), 500);
    }, 4000);
});

// ── Confirm delete ─────────────────────────────────────
document.querySelectorAll('[data-confirm]').forEach(el => {
    el.addEventListener('click', e => {
        if (!confirm(el.dataset.confirm)) e.preventDefault();
    });
});
