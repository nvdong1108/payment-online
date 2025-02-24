package com.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.model.Member;
import com.demo.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
	@Autowired
	private MemberRepository memberRepository;

	public List<Member> getAllMembers() {
		return memberRepository.findAll();
	}

	public Member createMember(Member member) {
		return memberRepository.save(member);
	}

	public void deleteMember(Long id) {
		memberRepository.deleteById(id);
	}

	public Member updateMember(Long id, Member updatedMember) {
		Optional<Member> existingMemberOpt = memberRepository.findById(id);
		if (existingMemberOpt.isPresent()) {
			Member existingMember = existingMemberOpt.get();
//			existingMember.setFullName(updatedMember.getFullName());
//			existingMember.setEmail(updatedMember.getEmail());
//			existingMember.setPhone(updatedMember.getPhone());
//			existingMember.setPassword(updatedMember.getPassword());
			return memberRepository.save(existingMember);
		}
		return null;
	}
}