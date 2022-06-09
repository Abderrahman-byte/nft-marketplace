/**
 * This is a wrapper for WebSocket class that handle messages as events.
 * 
 * if the received message is [eventName:string, eventPayload:any]
 * then an event is dispached from the event name.
 * 
*/
export class EventWebSocket extends EventTarget {
    CLOSED = WebSocket.CLOSED
    CLOSING = WebSocket.CLOSING
    CONNECTING = WebSocket.CONNECTING
    OPEN = WebSocket.OPEN

    constructor (url) {
        super()
        this.startConnection(url)
    }

    startConnection (url) {
        this.ws = new WebSocket(url)

        this.ws.addEventListener('message', e => {
            try {
                const [eventName, data] = JSON.parse(e.data)
                const event = new Event(eventName)

                event.data = data
                event.source = e.source
                event.ports = e.ports
                event.lastEventId = e.lastEventId
                event.origin = e.origin
    
                this.dispatchEvent(event)
            } catch {}
        })

        this.ws.addEventListener('message', e => {
            const event = new Event('packet')

            event.data = e.data
            event.source = e.source
            event.ports = e.ports
            event.lastEventId = e.lastEventId
            event.origin = e.origin

            this.dispatchEvent(event)
        })

        this.ws.addEventListener('error', e => {
            const event = new Event('error')
            this.dispatchEvent(event)
        })

        this.ws.addEventListener('open', e => {
            const event = new Event('open')
            this.dispatchEvent(event)
        })

        this.ws.addEventListener('close', e => {
            const event = new CloseEvent(e.type, {
				code: e.code,
				reason: e.reason,
				wasClean: e.wasClean,
			})

            this.dispatchEvent(event)
        })
    }

    send (data) {
        this.ws.send(data)
    }

    close () {
        this.ws.close()
    }

    get readyState () {
        return this.ws.readyState
    }
}