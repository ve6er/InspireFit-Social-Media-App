from fastapi import FastAPI
from pydantic import BaseModel
import mysql.connector
from nltk.sentiment.vader import SentimentIntensityAnalyzer


import nltk
# nltk.downloader.download('vader_lexicon')
# Initialize the FastAPI app
app = FastAPI()

# Database connection function
def get_db_connection():
    return mysql.connector.connect(
        host="localhost",
        user="root",
        password="danny3Ric",
        database="fitness_app"
    )

# Pydantic model for receiving comment IDs
class CommentID(BaseModel):
    id: int

class Sentiment:
    @staticmethod
    def analyze_sentiment(comment):
        sid = SentimentIntensityAnalyzer()
        sentiment_scores = sid.polarity_scores(comment)
        if sentiment_scores['compound'] >= 0.3:
            return 'positive'
        elif sentiment_scores['compound'] <= 0.04:
            return 'negative'
        else:
            return 'neutral'


# API Endpoint to filter comments
@app.get("/filter-comment/")
def moderate_comment(comment: str):
    return True if Sentiment.analyze_sentiment(comment) != 'negative' else False


# Save this code in a file named 'main.py' and run it with:
# uvicorn main:app --reload
