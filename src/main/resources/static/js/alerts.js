$(TriggerAlertClose());

function TriggerAlertClose() {
    const fadeOutTime = 500;
    const fadeInTime = 500;
    const infoTimeout = 3000;
    const successTimeout = 5000;
    const errorTimeout = 10000;

    $('.alert-info').hide().fadeIn(fadeInTime);
    $('.alert-success').hide().fadeIn(fadeInTime);
    $('.alert-danger').hide().fadeIn(fadeInTime);

    $('.alert').click(function () {
        $(this)
            .fadeOut(fadeOutTime, function () {
                $(this).remove();
            });
    });

    setTimeout(function () {
        $('.alert-info')
            .fadeOut(fadeOutTime, function () {
                $(this).remove();
            });
    }, infoTimeout);

    setTimeout(function () {
        $('.alert-success')
            .fadeOut(fadeOutTime, function () {
                $(this).remove();
            });
    }, successTimeout);

    setTimeout(function () {
        $('.alert-danger')
            .fadeOut(fadeOutTime, function () {
                $(this).remove();
            });
    }, errorTimeout);
}
