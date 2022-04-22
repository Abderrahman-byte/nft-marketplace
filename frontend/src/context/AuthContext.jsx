import React, { createContext, useContext, useEffect, useState } from 'react'
import LoadingCard from '../components/LoadingCard'

import { isUserLoggedIn } from '../utils/api'

export const AuthContext = createContext()

export const AuthProvider = ({ children }) => {
    const [authenticated, setAuth] = useState(undefined)
    const [isActive, setActive] = useState(false)
    const [modelElt, setModelElt] = useState(null)

    const openModel = (elt) => {
        setModelElt(elt)
        setActive(true)
    }

    const closeModel = () => {
        setModelElt(null)
        setActive(false)
    }

    const getAuthState = async () => {
        openModel(<LoadingCard />)

        const isLoggedIn = await isUserLoggedIn()
        setAuth(isLoggedIn)
        
        closeModel()
    }
    
    useEffect(() => {
        console.log('isLoggedIn')
        getAuthState()
    }, [])

    return (
        <AuthContext.Provider value={{ openModel, closeModel, authenticated, setAuth }}>
            {children}
            <div className={`model-background ${isActive ? 'active' : ''}`}></div>
            <div className={`model-popup-box ${isActive ? 'active' : ''}`}>
                {modelElt}
            </div>
        </AuthContext.Provider>
    )
}