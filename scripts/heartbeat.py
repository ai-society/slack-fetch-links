import requests
import time

url = 'https://ai-society-server.herokuapp.com/'
service = 'heartbeat'

while True:
	try:
		r = requests.get(url + service)
		time.sleep(60 * 10)
	except Exception:
		time.sleep(0.5)
		pass