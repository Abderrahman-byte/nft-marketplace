import React, { useEffect, createContext, useState, useContext, useCallback } from 'react'

import { EventWebSocket } from '@Utils/EventWebSocket'
import { AuthContext } from './AuthContext'
import { buildWebsocketApiUrl } from '@Utils/api'

export const NotificationsContext = createContext()

export const NotificationsProvider = ({ children }) => {
    const { authenticated } = useContext(AuthContext)
    const [socket, setSocket] = useState()
    const [notications, setNotifications] = useState([])

    const addNotification = useCallback (e => setNotifications([JSON.parse(e.data),...notications]), [notications])

    const addNotificationsList = useCallback(e => setNotifications([...e.data, ...notications]), [notications])

    useEffect(() => {
        if (!authenticated) return

        const ws = new EventWebSocket(buildWebsocketApiUrl('/notifications'))

        setSocket(socket)

        ws.addEventListener('notifications', addNotificationsList)

        ws.addEventListener('notification', e => addNotification)

        return () => {
            if (ws.readyState !== ws.CLOSED && ws.readyState !== ws.CLOSING) ws.close()
        }
    }, [authenticated])

    useEffect(() => {
        console.log('NOTIFICATIONS =>', notications)
    }, [notications]) 

    return (
        <NotificationsContext.Provider value={{ notications }}>
            {children}
        </NotificationsContext.Provider>
    )
}