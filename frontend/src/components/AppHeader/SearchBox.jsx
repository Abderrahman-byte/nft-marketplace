import React from 'react'

import '@Styles/SearchBox.css'

const SearchBox = () => {
    return (
        <div className='SearchBox'>
            <input name='search' placeholder='Search' className='SearchBox-input' autoComplete='off' />
            <button className='SearchBox-button'>
                <i className='search-icon'></i>
            </button>
        </div>
    )
}

export default SearchBox