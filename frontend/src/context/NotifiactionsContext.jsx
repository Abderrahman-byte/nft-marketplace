import React, { useEffect, createContext, useState, useContext } from 'react'

import { EventWebSocket } from '@/utils/EventWebSocket'
import { AuthContext } from './AuthContext'
import { buildWebsocketApiUrl } from '@/utils/api'

export const NotificationsContext = createContext()

export const NotificationsProvider = ({ children }) => {
    const { authenticated } = useContext(AuthContext)
    const [socket, setSocket] = useState()
    const [notications, setNotifications] = useState([])

    useEffect(() => {
        if (!authenticated) return

        const ws = new EventWebSocket(buildWebsocketApiUrl('/notifications'))

        setSocket(socket)

        ws.addEventListener('notifications', e => {
            setNotifications([...e.data, ...notications])
        })

        ws.addEventListener('notification', e => {
            setNotifications([JSON.parse(e.data),...notications])
        })

        ws.addEventListener('packet', e => {
            console.log('MESSAGE => ', JSON.parse(e.data))
        })

        return () => {
            if (ws.readyState !== ws.CLOSED && ws.readyState !== ws.CLOSING) ws.close()
        }
    }, [authenticated])

    useEffect(() => {
        console.log('NOTIFICATIONS', notications)
    }, [notications]) 

    return (
        <NotificationsContext.Provider value={{ notications }}>
            {children}
        </NotificationsContext.Provider>
    )
}