export const passwordResetHtml = (code: string | number) => {
    return `
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Password Reset</title>
            <style>
                body {
                    font-family: Arial, sans-serif;
                    background-color: #f0f0f0;
                    margin: 0;
                    padding: 0;
                }
                .email-container {
                    background-color: #ffffff;
                    padding: 20px;
                    border-radius: 8px;
                    max-width: 600px;
                    margin: 20px auto;
                    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                }
                .email-header {
                    font-size: 24px;
                    margin-bottom: 20px;
                    color: #333333;
                }
                .email-content {
                    font-size: 16px;
                    margin-bottom: 20px;
                    color: #666666;
                }
                .reset-code {
                    display: inline-block;
                    font-size: 32px;
                    font-weight: bold;
                    background-color: #f7f7f7;
                    padding: 10px;
                    border-radius: 4px;
                    letter-spacing: 2px;
                    color: #007bff;
                }
                .email-footer {
                    font-size: 14px;
                    color: #999999;
                    margin-top: 20px;
                }
            </style>
        </head>
        <body>
            <div class="email-container">
                <div class="email-header">Password Reset Request</div>
                <div class="email-content">
                    We received a request to reset your password. Please use the following code to reset your password:
                </div>
                <div class="reset-code">${code}</div>
                <div class="email-content">
                    If you did not request a password reset, please ignore this email or contact support if you have any concerns.
                </div>
                <div class="email-footer">
                    Thank you,<br>
                    MuscleMemo
                </div>
            </div>
        </body>
        </html>
    `;
};

export const reportHtml = (
    reporter: number,
    reportee: number,
    reason: string
) => {
    return `
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Reported User Notification</title>
            <style>
                body {
                    font-family: Arial, sans-serif;
                    background-color: #f0f0f0;
                    margin: 0;
                    padding: 0;
                }
                .email-container {
                    background-color: #ffffff;
                    padding: 20px;
                    border-radius: 8px;
                    max-width: 600px;
                    margin: 20px auto;
                    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                }
                .email-header {
                    font-size: 24px;
                    margin-bottom: 20px;
                    color: #333333;
                }
                .email-content {
                    font-size: 16px;
                    margin-bottom: 20px;
                    color: #666666;
                }
                .user-info {
                    font-size: 16px;
                    background-color: #f7f7f7;
                    padding: 10px;
                    border-radius: 4px;
                    color: #333333;
                }
                .email-footer {
                    font-size: 14px;
                    color: #999999;
                    margin-top: 20px;
                }
            </style>
        </head>
        <body>
            <div class="email-container">
                <div class="email-header">User Report Notification</div>
                <div class="email-content">
                    An issue has been reported regarding a user. Below are the details of the reported user and the report:
                </div>
                <div class="user-info">
                    <strong>Reported User Id:</strong> ${reportee}<br>
                    <strong>Reported By:</strong> ${reporter}<br>
                    <strong>Reason:</strong> ${reason}
                </div>
                <div class="email-content">
                    Please review the details and take appropriate action as necessary.
                </div>
                <div class="email-footer">
                    Thank you,<br>
                    MuscleMemo
                </div>
            </div>
        </body>
        </html>
    `;
};
