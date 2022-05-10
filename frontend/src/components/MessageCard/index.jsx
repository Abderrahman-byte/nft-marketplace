import React from 'react'

import './styles.css'

const ErrorCard = ({ title, text, closeBtnCallback }) => {
    return (
        <div className='MessageCard card'>
            <h2>{title}</h2>
            <p className='message'>{text}</p>
            <button className='btn btn-blue block' onClick={closeBtnCallback}>Close</button>
        </div>
    )
}

export default ErrorCard