const canvas = document.getElementById('fireworks');
const ctx = canvas.getContext('2d');

function resizeCanvas() {
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;
}
resizeCanvas();
window.addEventListener('resize', resizeCanvas);

class Particle {
    constructor(x, y, color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.size = Math.random() * 3 + 2; 
        this.velocity = {
            x: (Math.random() - 0.5) * 10, 
            y: (Math.random() - 0.5) * 10
        };
        this.alpha = 1;
        this.friction = 0.96; 
        this.gravity = 0.08; 
    }

    draw() {
        ctx.globalAlpha = this.alpha;
        ctx.beginPath();
        ctx.arc(this.x, this.y, this.size, 0, Math.PI * 2, false);
        ctx.fillStyle = this.color;
        ctx.fill();
        ctx.globalAlpha = 1;
    }

    update() {
        this.velocity.x *= this.friction;
        this.velocity.y *= this.friction;
        this.velocity.y += this.gravity; 
        this.x += this.velocity.x;
        this.y += this.velocity.y;
        this.alpha -= 0.015; 

        this.draw();
    }
}

class Firework {
    constructor(x, y, color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.velocity = { x: (Math.random() - 0.5) * 2, y: Math.random() * -4 - 4 }; 
        this.particles = [];
        this.lifespan = 80 + Math.random() * 40; 
        this.hasExploded = false;
    }

    draw() {
        ctx.beginPath();
        ctx.arc(this.x, this.y, 4, 0, Math.PI * 2, false); 
        ctx.fillStyle = this.color;
        ctx.fill();
    }

    explode() {
        const explosionSize = Math.random() * 20 + 50;
        for (let i = 0; i < explosionSize; i++) {
            this.particles.push(new Particle(this.x, this.y, this.color));
        }
    }

    update() {
        if (!this.hasExploded) {
            this.y += this.velocity.y;
            this.velocity.y += 0.02;
    
            if (this.lifespan-- <= 0) {
                this.explode();
                this.hasExploded = true;
            }
        }
    
        for (let i = this.particles.length - 1; i >= 0; i--) {
            let p = this.particles[i];
            p.update();
            if (p.alpha <= 0) this.particles.splice(i, 1);
        }
    
        if (!this.hasExploded) this.draw();
    }
    
}

let fireworks = [];
function animate() {
    requestAnimationFrame(animate);

    ctx.fillStyle = 'rgba(255, 255, 255, 0.62)';
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    // Create a new array, keeping only active fireworks
    fireworks = fireworks.filter(firework => !firework.hasExploded || firework.particles.length > 0);

    for (let firework of fireworks) {
        firework.update();
    }

    if (Math.random() < 0.03) {
        const x = Math.random() * canvas.width;
        const color = `hsl(${Math.random() * 360}, 70%, 70%)`;
        fireworks.push(new Firework(x, canvas.height, color));
    }
}



animate();
