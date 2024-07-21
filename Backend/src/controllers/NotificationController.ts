import { NextFunction, Request, Response } from "express";
import jwt from "jsonwebtoken";
import { failure, success } from "../utils/responseTypes";
import { verifyRefreshToken } from "../utils/token";
import { getMessaging } from '../firebaseAdmin';

export class NotificationController {
    async notification(request: Request, response: Response, next: NextFunction) {
        const {registrationToken, title, message} = request.body
        const messageData = {
            data: {
              title: title,
              message: message
            },
            token: registrationToken
          };
        console.log(messageData);
        const yert = await getMessaging().send(messageData);
        return await success(true)
    }

    
}
