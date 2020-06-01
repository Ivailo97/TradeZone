package TradeZone.web.controller;

import TradeZone.service.ChannelService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/channel")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class ChannelController {

    private final ChannelService channelService;

    @PostMapping("/create")
    public ResponseEntity<?> createChannel(@RequestBody String channelId) {
        channelService.create(channelId);
        return ResponseEntity.ok().build();
    }
}
