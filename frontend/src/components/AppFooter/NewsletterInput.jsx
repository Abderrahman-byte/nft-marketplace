import React from 'react'

import '@Styles/NewsletterInput.css'

const NewsletterInput = () => {
    return (
        <div className='NewsletterInput'>
            <input className='NewsletterInput-input' type='email' name='email' placeholder='Enter your email' autoComplete='off' />
            <button className='NewsletterInput-btn'>
                <i className='arrow-right-icon'></i>
            </button>
        </div>
    )
}

export default NewsletterInput