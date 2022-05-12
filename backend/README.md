# NFT marketplace

## Endpoints :

 + /verify-email
 + /api/{version}
   * /auth
     * POST /login
     * POST /register
     * GET /isLoggedIn
     * POST/GET /logout
   * /profile
     * POST .
     * GET .
     * POST /picture
   * /marketplace
     * GET /tokens
     * POST /tokens
     * GET /tokens/{tokenId}
     * GET /tokens/{tokenId}/bids
     * POST /bids
     * GET /bids?ref=<JWT> (Create Bid stream)
     * POST /bids/{bidId}
     * POST /buy
     * GET /buy?ref=<JWT> (Create transaction stream)
     * GET /collections
     * POST /collections
     * GET /collections/{collectionId}
     * GET /collections/{collectionId}/items
     * POST /like
     * DELETE /like
   * /user
     * GET /{userId}
     * GET /{userId}/tokens