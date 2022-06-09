import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router'

const SearchList = ({ searchCallback, container }) => {
    const [query, setQuery] = useState()
    const [page, setPage] = useState(1)
    const params = useParams()
    const [isLoading, setLoading] = useState(false)
    const [items, setItems] = useState([])
    const itemsPerPage = 10

    const fetchSearchResults = async () => {
        if (typeof searchCallback !== 'function' || isLoading) return

        setLoading(true)
        
        const results = await searchCallback(query, page)
        setItems(items => [...items, ...results])

        if (results.length < itemsPerPage) setPage(0)

        setLoading(false)
    }

    useEffect(() => {
        if (!query || page <= 0) return

        fetchSearchResults()
    }, [page, query])

    useEffect(() => {
        if (!query) return

        setItems([])
        setPage(0)

        setTimeout(() => setPage(1), 1)
    }, [query, searchCallback])

    useEffect(() => {
        if(!params?.query) return
        setQuery(params?.query)
    }, [params])

    if (!container || items.length <= 0) return <></>

    return <>
        {container({ data: items })}

        {page > 0 && !isLoading ? (
            <button onClick={() => setPage(p => p + 1)} className='btn btn-blue block more-btn'>more</button>
        ) : null}
    </>
}

export default SearchList