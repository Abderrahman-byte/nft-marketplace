import React, { createContext, useState } from 'react'

export const ModelContext = createContext({})

export const ModelProvider = ({children}) => {
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

    return (
        <ModelContext.Provider value={{ openModel, closeModel }}>
            {children}
            <div className={`model-background ${isActive ? 'active' : ''}`}></div>
            <div className={`model-popup-box ${isActive ? 'active' : ''}`}>
                {modelElt}
            </div>
        </ModelContext.Provider>
    )
}