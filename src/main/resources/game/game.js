// noinspection JSJQueryEfficiency

$(document).ready(function() {
    let score = 0;
    let isJumping = false;
    let obstacleSpeed = 10;
    let gameInterval;

    function startGame() {
        $('.obstacle').css('right', '-40px');
        score = 0;
        $('#score').text(score);
        obstacleSpeed = 5;
        $('.game-over').hide();
        gameInterval = setInterval(moveObstacle, 20);
    }

    function moveObstacle() {
        let obstaclePosition = parseInt($('.obstacle').css('right'));

        if (obstaclePosition > $(window).width()) {
            $('.obstacle').css('right', '-40px');
            score++;
            $('#score').text(score);
            increaseDifficulty();
        } else {
            $('.obstacle').css('right', obstaclePosition + obstacleSpeed + 'px');
        }

        if (checkCollision()) {
            clearInterval(gameInterval);
            $('#final-score').text(score);
            $('.game-over').show();
        }
    }

    function increaseDifficulty() {
        if (score % 5 === 0) {
            obstacleSpeed += 1;
        }
    }

    function checkCollision() {
        let cat = $('.cat');
        let obstacle = $('.obstacle');

        let catBottom = cat.offset().top + cat.height();
        let catLeft = cat.offset().left;
        let catRight = catLeft + cat.width();

        let obstacleTop = obstacle.offset().top;
        let obstacleLeft = obstacle.offset().left;
        let obstacleRight = obstacleLeft + obstacle.width();

        return (catBottom >= obstacleTop && catLeft < obstacleRight && catRight > obstacleLeft);
    }

    function jump() {
        if (!isJumping) {
            isJumping = true;
            $('.cat').animate({ bottom: '+=200px' }, 500, function() {
                $('.cat').animate({ bottom: '-=200px' }, 500, function() {
                    isJumping = false;
                });
            });
        }
    }

    $(document).keydown(function(event) {
        if (event.key === ' ') {
            jump();
        }
    });

    $(document).click(function() {
        jump();
    });

    startGame();
});