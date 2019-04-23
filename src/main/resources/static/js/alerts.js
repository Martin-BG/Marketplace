$(document).ready(TriggerAlertClose());

function TriggerAlertClose() {
    const fadeOutTime = 300;
    const infoTimeout = 3000;
    const successTimeout = 5000;
    const errorTimeout = 7000;

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
