package com.demo.controller.api;

import com.demo.model.Member;
import com.demo.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")

public class ApiMemberController {

    @Autowired
    private MemberService memberService;


    @GetMapping
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }


    @PostMapping
    public ResponseEntity<Member> createMember(@RequestBody Member member) {

        Member createdMember = memberService.createMember(member);
        return ResponseEntity.ok(createdMember);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @RequestBody Member updatedMember) {
        Member member = memberService.updateMember(id, updatedMember);
        if (member != null) {
            return ResponseEntity.ok(member);
        }
        return ResponseEntity.notFound().build();
    }

}
