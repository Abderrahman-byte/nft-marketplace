import React, { useState } from 'react'
import { useNavigate } from 'react-router'

const SearchBox = () => {
    const [query, setQuery] = useState('')
    const navigate = useNavigate()

    const submitCallback = e => {
        e.preventDefault()

        if (!query && query.length <= 0) return

        navigate(`/search/${query}/items`)
    }

    return (
        <form onSubmit={submitCallback} className='SearchBox'>
            <input value={query} onChange={e => setQuery(e.target.value)} name='search' placeholder='Search' className='SearchBox-input' autoComplete='off' />
            <button className='SearchBox-button'>
                <i className='search-icon'></i>
            </button>
        </form>
    )
}

export default SearchBox