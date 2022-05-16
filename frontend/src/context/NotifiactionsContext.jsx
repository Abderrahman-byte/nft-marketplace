import React, { useEffect, createContext, useState, useContext, useCallback } from 'react'

import { EventWebSocket } from '@Utils/EventWebSocket'
import { AuthContext } from './AuthContext'
import { buildWebsocketApiUrl } from '@Utils/api'

export const NotificationsContext = createContext()

export const NotificationsProvider = ({ children }) => {
    const { authenticated } = useContext(AuthContext)
    const [socket, setSocket] = useState()
    const [notifications, setNotifications] = useState([])

    const addNotification = useCallback (e => {
        setNotifications(prevNotifications => [JSON.parse(e.data),...prevNotifications])
    }, [])

    const addNotificationsList = useCallback(e => {
        setNotifications(prevNotifications => [...e.data, ...prevNotifications])
    }, [])

    const sendVuedEvent = timestamp => {
        if (socket) socket.send(JSON.stringify(['vued', timestamp]))
    }

    const setVued = id => {
        setNotifications(notifications.map(notification => {
            if (notification.id === id) notification.vued = true

            return {...notification}
        }))
    }

    useEffect(() => {
        if (!authenticated) return

        const ws = new EventWebSocket(buildWebsocketApiUrl('/notifications'))

        setSocket(ws)

        ws.addEventListener('notifications', addNotificationsList)

        ws.addEventListener('notification', addNotification)

        return () => {
            if (ws.readyState !== ws.CLOSED && ws.readyState !== ws.CLOSING) ws.close()
        }
    }, [authenticated])

    return (
        <NotificationsContext.Provider value={{ notifications, sendVuedEvent, setVued }}>
            {children}
        </NotificationsContext.Provider>
    )
}