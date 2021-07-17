package hello.itemservice.web.form;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.ItemType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/form/items")
@RequiredArgsConstructor
public class FormItemController {

    private final ItemRepository itemRepository;

    /** 모든 요청에 담기는 모델 정의.  해당 컨트롤러의 모든 메소드를 호출할 때, regions 이름의 모델이 담긴다.  */
    @ModelAttribute("regions")
    public Map<String, String> regions(){ // 배송 종류
        Map<String, String> regions = new LinkedHashMap<>(); // 순서가 보장되는 LinkedHashMap
        regions.put("SEOUL", "서울"); // {키, 사용자에게 보여주는 값}
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");
        return regions;
        // 자주 쓰이는 모델을 쓸 때 @ModelAttribute("regions") 사용.
    }

    @ModelAttribute("itemTypes")
    public ItemType[] itemTypes(){ // 상품 종류 배열
        ItemType[] values = ItemType.values(); /** enum의 모든 정보를 배열로 돌려줌 */
        return values;
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "form/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "form/item";
    }

    @GetMapping("/add")
    public String addForm(Model model)
    {
        // 빈 모델 넘겨주기
        model.addAttribute("item", new Item());
        return "form/addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        log.info("item.getOpen={}", item.getOpen());
        log.info("item.regions={}", item.getRegions()); // 등록 지역 리스트
        log.info("item.itemTYpe={}", item.getItemType()); // 상품 종류 (단일 선택 라디오 버튼) 체크 안하면, null이다.

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/form/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "form/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/form/items/{itemId}";
    }

}

