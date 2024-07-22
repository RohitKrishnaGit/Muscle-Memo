import { OAuth2Client } from "google-auth-library";
import { createTransport } from "nodemailer";
import { getEnv } from "../environment";

let creds: OAuth2Client;

export const initEmailService = () => {
    creds = new OAuth2Client(
        getEnv().G_CLIENT_ID,
        getEnv().G_CLIENT_SECRET,
        getEnv().G_REDIRECT_URI
    );
    creds.setCredentials({
        refresh_token: getEnv().G_REFRESH_TOKEN,
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
            clientId: getEnv().G_CLIENT_ID,
            clientSecret: getEnv().G_CLIENT_SECRET,
            refreshToken: getEnv().G_REFRESH_TOKEN,
            accessToken: token,
        },
    });
    return transport.sendMail(mailOptions);
};
