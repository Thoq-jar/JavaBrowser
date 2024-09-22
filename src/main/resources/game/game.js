$(document).ready(function () {
    let score = 0;
    let isJumping = false;
    let obstacleSpeed = 10;
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
        obstacleSpeed = 5;
        $gameOver.hide();
        gameInterval = setInterval(moveObstacle, 3);
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
        if (score % 5 === 0) obstacleSpeed += 0.5;
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
            $cat.animate({bottom: '+=200px'}, 500, function () {
                $cat.animate({bottom: '-=200px'}, 500, function () {
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