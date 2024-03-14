package bytebrewers.bitpod.controller;

import bytebrewers.bitpod.entity.Portfolio;
import bytebrewers.bitpod.entity.Transaction;
import bytebrewers.bitpod.service.PortfolioService;
import bytebrewers.bitpod.utils.constant.ApiUrl;
import bytebrewers.bitpod.utils.constant.Messages;
import bytebrewers.bitpod.utils.dto.PageResponseWrapper;
import bytebrewers.bitpod.utils.dto.Res;
import bytebrewers.bitpod.utils.dto.request.portfolio.PortfolioDTO;
import bytebrewers.bitpod.utils.dto.request.transaction.TransactionDTO;
import bytebrewers.bitpod.utils.swagger.portfolio.SwaggerPortfolioCurrent;
import bytebrewers.bitpod.utils.swagger.portfolio.SwaggerPortfolioIndex;
import bytebrewers.bitpod.utils.swagger.portfolio.SwaggerPortfolioShow;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiUrl.BASE_URL + ApiUrl.BASE_PORTFOLIO)
@RequiredArgsConstructor
@Tag(name = "Portfolio", description = "Portfolio API")
public class PortfolioController {
    private final PortfolioService portfolioService;

    @SwaggerPortfolioIndex
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping
    public ResponseEntity<?> index(
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @ModelAttribute PortfolioDTO portfolioDTO
    ) {
        Page<Portfolio> res = portfolioService.getAll(pageable, portfolioDTO);
        PageResponseWrapper<Portfolio> responseWrapper = new PageResponseWrapper<>(res);
        return Res.renderJson(responseWrapper, Messages.PORTFOLIO_FOUND, HttpStatus.OK);
    }

    @SwaggerPortfolioShow
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable String id) {
        Portfolio portfolio = portfolioService.getById(id);
        return Res.renderJson(portfolio, Messages.PORTFOLIO_FOUND, HttpStatus.OK);
    }

    @SwaggerPortfolioCurrent
    @GetMapping("/current")
    public ResponseEntity<?> showByCurrentUser(@RequestHeader(name = "Authorization") String token) {
        Portfolio portfolio = portfolioService.currentUser(token);
        return Res.renderJson(portfolio, Messages.PORTFOLIO_FOUND, HttpStatus.OK);
    }
}
