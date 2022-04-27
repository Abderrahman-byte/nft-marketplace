import React from 'react'

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