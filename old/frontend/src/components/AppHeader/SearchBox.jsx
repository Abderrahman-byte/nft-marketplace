import React, { useState } from 'react'
import { useNavigate } from 'react-router'

const SearchBox = ({isOpenSearch, setOpenSearch}) => {
    const [query, setQuery] = useState('')
    const navigate = useNavigate()
    

    const submitCallback = e => {
        e.preventDefault()

        if (!query && query.length <= 0) return

        navigate(`/search/${query}/items`)
    }

    return (
        
        <form onSubmit={submitCallback} className='SearchBox'>
            <input value={query} onChange={e => setQuery(e.target.value)} name='search' placeholder='Search' className={`SearchBox-input  ${isOpenSearch? 'open' :''}`} autoComplete='off' />
            <button onClick={() => setOpenSearch(true)} className='SearchBox-button '>
                <i className='search-icon'></i>
            </button>
        </form>
        
    )
}

export default SearchBox