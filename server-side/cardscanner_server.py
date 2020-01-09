import requests
import json
import firebase_admin
from firebase_admin import credentials, firestore
import datetime
import time

notification_threshold = 2000


def send_message(days, card_no, token):
    headers = {
        'Content-Type': 'application/json',
        "Authorization": "key=AAAAZfmAwCg:APA91bHz46G2irpmyGd7D-W_-u7F2huBv0SCOzUBQQ"
                         "-qhLmzvKfqDvWqFtgpW5sygsM68DBQ2JTjJJ5-MH6GE5neBnMQaY"
                         "-MEbHLYKaOGr6gURbboB19Lf2etC82X7QezOnG8IFdyf1s"
    }

    data = {
        "notification": {
            "body": "card: {} will be expired in {} days".format(card_no, days),
            "title": "Expire Card",
            "sound": "default"},
        "to": token

    }
    response = requests.post('https://fcm.googleapis.com/fcm/send', headers=headers, data=json.dumps(data))
    print(response)
    return response.status_code


def monitor_firestore():
    cred = credentials.Certificate("cardscanner-server.json")
    default_app = firebase_admin.initialize_app(cred)
    db = firestore.client()
    doc_ref = db.collection(u'cards')
    while True:
        q = []
        docs = doc_ref.stream()
        for doc in docs:
            d = doc.to_dict()
            month = int(d["expireDate"].split("/")[0])
            year = int("20"+d["expireDate"].split("/")[1])
            notified=d["notified"]

            if notified=="F":
                if datetime.datetime(year, month, 1) - datetime.timedelta(days=notification_threshold) < datetime.datetime.now():
                    days = (datetime.datetime(year, month, 1) - datetime.datetime.now()).days
                    d["days"]=days
                    q.append((doc.id, d))
                    print("add in q", doc.id, d)

        time.sleep(15)
        while q:
            doc_id, d = q[-1]
            if send_message(d["days"],d["cardNumber"],d["token"]) == 200:
                print("succ push", doc_id, d)
                q.pop()
                doc_ref.document(doc_id).update({"notified":"T"})
            else:
                print("error with send notification")
                time.sleep(60)


monitor_firestore()


