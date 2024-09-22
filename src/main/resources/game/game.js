import '../lib/std.js';

$(document).ready(function () {
    let score = 0;
    let isJumping = false;
    let obstacleSpeed = 5;
    let gameInterval;
    let gameOver = false;

    const $obstacle = $('.obstacle');
    const $cat = $('.cat');
    const $score = $('#score');
    const $gameOver = $('.game-over');
    const $finalScore = $('#final-score');

    function startGame() {
        gameOver = false;
        $obstacle.css('right', '-40px');
        score = 0;
        $score.text(score);
        obstacleSpeed = 10;
        $gameOver.hide();
        gameLoop();
    }

    function gameLoop() {
        if (!gameOver) {
            moveObstacle();
            requestAnimationFrame(gameLoop);
        }
    }

    function moveObstacle() {
        let obstaclePosition = parseInt($obstacle.css('right'));

        if (obstaclePosition > $(window).width()) {
            $obstacle.css('right', '-40px');
            score++;
            $score.text(score);
            increaseDifficulty();
        } else {
            $obstacle.css('right', obstaclePosition + obstacleSpeed + 'px');
        }

        if (collision()) {
            clearInterval(gameInterval);
            endGame();
        }
    }

    function increaseDifficulty() {
        if (score % 1 === 0) obstacleSpeed += 1;
    }

    function collision() {
        let catBottom = $cat.offset().top + $cat.height();
        let catLeft = $cat.offset().left;
        let catRight = catLeft + $cat.width();

        let obstacleTop = $obstacle.offset().top;
        let obstacleLeft = $obstacle.offset().left;
        let obstacleRight = obstacleLeft + $obstacle.width();

        return (catBottom >= obstacleTop && catLeft < obstacleRight && catRight > obstacleLeft);
    }

    function jump() {
        if (!isJumping && !gameOver) {
            isJumping = true;
            $cat.animate({bottom: '+=400px'}, 300, function () {
                $cat.animate({bottom: '-=400px'}, 300, function () {
                    isJumping = false;
                });
            });
        } else if (gameOver) {
            startGame();
        }
    }

    function endGame() {
        gameOver = true;
        isJumping = false;
        $finalScore.text(score);
        $gameOver.show();
    }

    $(document).keydown(function (event) {
        if (event.key === ' ') jump();
    });

    $(document).click(function () {
        jump();
    });

    startGame();
});