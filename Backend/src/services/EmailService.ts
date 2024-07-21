import { OAuth2Client } from "google-auth-library";
import { createTransport } from "nodemailer";

let creds: OAuth2Client;

export const initEmailService = () => {
    creds = new OAuth2Client(
        process.env.G_CLIENT_ID,
        process.env.G_CLIENT_SECRET,
        process.env.G_REDIRECT_URI
    );
    creds.setCredentials({
        refresh_token: process.env.G_REFRESH_TOKEN,
    });
};

export const sendEmail = async (
    recipient: string,
    subject: string,
    html: string
) => {
    const { token } = await creds.getAccessToken();
    if (!token) {
        throw new Error("token is null");
    }
    const mailOptions = {
        from: "MuscleMemo <musclememo.help@gmail.com>",
        to: recipient,
        subject,
        html,
    };

    const transport = createTransport({
        service: "gmail",
        auth: {
            type: "oauth2",
            user: "musclememo.help@gmail.com",
            clientId: process.env.G_CLIENT_ID,
            clientSecret: process.env.G_CLIENT_SECRET,
            refreshToken: process.env.G_REFRESH_TOKEN,
            accessToken: token,
        },
    });
    return transport.sendMail(mailOptions);
};
