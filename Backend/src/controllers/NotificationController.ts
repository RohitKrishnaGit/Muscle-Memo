import { NextFunction, Request, Response } from "express";
import jwt from "jsonwebtoken";
import { failure, success } from "../utils/responseTypes";
import { verifyRefreshToken } from "../utils/token";
import { getMessaging } from '../firebaseAdmin';

export class NotificationController {
  async notificationHelper(registrationToken: string, title: string, message: string) {
    const messageData = {
        data: {
          title: title,
          message: message
        },
        token: registrationToken
      };
    await getMessaging().send(messageData);
    return success(true);
}

    async notification(request: Request, response: Response, next: NextFunction) {
        const {registrationToken, title, message} = request.body
        return this.notificationHelper(registrationToken, title, message);
    }

    
}
